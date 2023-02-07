package com.grash.service;

import com.grash.advancedsearch.SearchCriteria;
import com.grash.advancedsearch.SpecificationBuilder;
import com.grash.dto.AssetPatchDTO;
import com.grash.dto.AssetShowDTO;
import com.grash.dto.imports.AssetImportDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.AssetMapper;
import com.grash.model.*;
import com.grash.model.enums.AssetStatus;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.AssetRepository;
import com.grash.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private LocationService locationService;
    private final FileService fileService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final UserService userService;
    private final CompanyService companyService;
    private final NotificationService notificationService;
    private final TeamService teamService;
    private final AssetMapper assetMapper;
    private final EntityManager em;
    private final AssetDowntimeService assetDowntimeService;

    @Autowired
    public void setDeps(@Lazy LocationService locationService
    ) {
        this.locationService = locationService;
    }

    @Transactional
    public Asset create(Asset asset) {
        Asset savedAsset = assetRepository.saveAndFlush(asset);
        em.refresh(savedAsset);
        return savedAsset;
    }

    @Transactional
    public Asset update(Long id, AssetPatchDTO asset) {
        if (assetRepository.existsById(id)) {
            Asset savedAsset = assetRepository.findById(id).get();
            Asset patchedAsset = assetRepository.saveAndFlush(assetMapper.updateAsset(savedAsset, asset));
            em.refresh(patchedAsset);
            return patchedAsset;
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    public Collection<Asset> getAll() {
        return assetRepository.findAll();
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }

    public Optional<Asset> findById(Long id) {
        return assetRepository.findById(id);
    }

    public Collection<Asset> findByCompany(Long id) {
        return assetRepository.findByCompany_Id(id);
    }

    public Collection<Asset> findAssetChildren(Long id) {
        return assetRepository.findByParentAsset_Id(id);
    }

    public boolean hasAccess(OwnUser user, Asset asset) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(asset.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Asset assetReq) {
        Long companyId = user.getCompany().getId();
        //@NotNull fields
        boolean first = companyService.isCompanyValid(assetReq.getCompany(), companyId);

        return first && canPatch(user, assetMapper.toPatchDto(assetReq));
    }

    public boolean canPatch(OwnUser user, AssetPatchDTO assetReq) {
        Long companyId = user.getCompany().getId();
        //optional fields
        boolean second = locationService.isLocationInCompany(assetReq.getLocation(), companyId, true);
        boolean third = fileService.isFileInCompany(assetReq.getImage(), companyId, true);
        boolean fourth = assetCategoryService.isAssetCategoryInCompany(assetReq.getCategory(), companyId, true);
        boolean fifth = isAssetInCompany(assetReq.getParentAsset(), companyId, true);
        boolean sixth = userService.isUserInCompany(assetReq.getPrimaryUser(), companyId, true);
        boolean seventh = deprecationService.isDeprecationInCompany(assetReq.getDeprecation(), companyId, true);
        boolean eighth = assetReq.getAssignedTo() == null || assetReq.getAssignedTo().stream().allMatch(user1 -> {
            Optional<OwnUser> optionalUser1 = userService.findById(user1.getId());
            return optionalUser1.map(value -> value.getCompany().getId().equals(companyId)).orElse(false);
        });

        return second && third && fourth && fifth && sixth && seventh && eighth;
    }

    public void notify(Asset asset, String message) {
        asset.getUsers().forEach(user -> notificationService.create(new Notification(message, user, NotificationType.ASSET, asset.getId())));
    }

    public void patchNotify(Asset oldAsset, Asset newAsset) {
        String message = "Asset " + newAsset.getName() + " has been assigned to you";
        oldAsset.getNewUsersToNotify(newAsset.getUsers()).forEach(user -> notificationService.create(
                new Notification(message, user, NotificationType.ASSET, newAsset.getId())));
    }

    public Collection<Asset> findByLocation(Long id) {
        return assetRepository.findByLocation_Id(id);
    }

    public void stopDownTime(Long id) {
        Asset savedAsset = findById(id).get();
        Collection<AssetDowntime> assetDowntimes = assetDowntimeService.findByAsset(id);
        Optional<AssetDowntime> optionalRunningDowntime = assetDowntimes.stream().filter(assetDowntime -> assetDowntime.getDuration() == 0).findFirst();
        if (optionalRunningDowntime.isPresent()) {
            AssetDowntime runningDowntime = optionalRunningDowntime.get();
            runningDowntime.setDuration(Helper.getDateDiff(runningDowntime.getStartsOn(), new Date(), TimeUnit.SECONDS));
            assetDowntimeService.save(runningDowntime);
        }
        savedAsset.setStatus(AssetStatus.OPERATIONAL);
        save(savedAsset);
        notify(savedAsset, savedAsset.getName() + " is now Operational");
    }

    public void triggerDownTime(Long id) {
        Asset asset = findById(id).get();
        AssetDowntime assetDowntime = AssetDowntime
                .builder()
                .startsOn(new Date())
                .asset(asset)
                .build();
        assetDowntimeService.create(assetDowntime);
        asset.setStatus(AssetStatus.DOWN);
        save(asset);
        notify(asset, asset.getName() + " is down");

    }

    public boolean isAssetInCompany(Asset asset, long companyId, boolean optional) {
        if (optional) {
            Optional<Asset> optionalAsset = asset == null ? Optional.empty() : findById(asset.getId());
            return asset == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        } else {
            Optional<Asset> optionalAsset = findById(asset.getId());
            return optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId);
        }
    }

    public Page<AssetShowDTO> findBySearchCriteria(SearchCriteria searchCriteria) {
        SpecificationBuilder<Asset> builder = new SpecificationBuilder<>();
        searchCriteria.getFilterFields().forEach(builder::with);
        Pageable page = PageRequest.of(searchCriteria.getPageNum(), searchCriteria.getPageSize(), searchCriteria.getDirection(), "id");
        return assetRepository.findAll(builder.build(), page).map(assetMapper::toShowDto);
    }

    public Optional<Asset> findByNameAndCompany(String assetName, Long companyId) {
        return assetRepository.findByNameAndCompany_Id(assetName, companyId);
    }

    public void importAsset(Asset asset, AssetImportDTO dto, Company company) {
        Long companySettingsId = company.getCompanySettings().getId();
        Long companyId = company.getId();
        asset.setArea(dto.getArea());
        asset.setBarCode(dto.getBarCode());
        asset.setArea(dto.getArea());
        asset.setArchived(Helper.getBooleanFromString(dto.getArchived()));
        Optional<Location> optionalLocation = locationService.findByNameAndCompany(dto.getLocationName(), companyId);
        optionalLocation.ifPresent(asset::setLocation);
        Optional<Asset> optionalAsset = findByNameAndCompany(dto.getParentAssetName(), companyId);
        optionalAsset.ifPresent(asset::setParentAsset);
        Optional<AssetCategory> optionalAssetCategory = assetCategoryService.findByNameAndCompanySettings(dto.getCategory(), companySettingsId);
        optionalAssetCategory.ifPresent(asset::setCategory);
        asset.setName(dto.getName());
        Optional<OwnUser> optionalPrimaryUser = userService.findByEmailAndCompany(dto.getPrimaryUserEmail(), companyId);
        optionalPrimaryUser.ifPresent(asset::setPrimaryUser);
        asset.setWarrantyExpirationDate(Helper.getDateFromExcelDate(dto.getWarrantyExpirationDate()));
        asset.setAdditionalInfos(dto.getAdditionalInfos());
        asset.setSerialNumber(dto.getSerialNumber());
        List<OwnUser> assignedTo = new ArrayList<>();
        dto.getAssignedToEmails().forEach(email -> {
            Optional<OwnUser> optionalUser1 = userService.findByEmailAndCompany(email, companyId);
            optionalUser1.ifPresent(assignedTo::add);
        });
        asset.setAssignedTo(assignedTo);
        List<Team> teams = new ArrayList<>();
        dto.getTeamsNames().forEach(teamName -> {
            Optional<Team> optionalTeam = teamService.findByNameAndCompany(teamName, companyId);
            optionalTeam.ifPresent(teams::add);
        });
        asset.setTeams(teams);
        asset.setStatus(AssetStatus.getAssetStatusFromString(dto.getStatus()));
        asset.setAcquisitionCost(dto.getAcquisitionCost());
        assetRepository.save(asset);
    }

    public Optional<Asset> findByIdAndCompany(Long id, Long companyId) {
        return assetRepository.findByIdAndCompany_Id(id, companyId);
    }
}
