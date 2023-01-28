package com.grash.service;

import com.grash.dto.AssetPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.AssetMapper;
import com.grash.model.*;
import com.grash.model.enums.AssetStatus;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.AssetRepository;
import com.grash.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final LocationService locationService;
    private final FileService fileService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final UserService userService;
    private final CompanyService companyService;
    private final NotificationService notificationService;
    private final AssetMapper assetMapper;
    private final EntityManager em;
    private final AssetDowntimeService assetDowntimeService;

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

        Optional<Location> optionalLocation = assetReq.getLocation() == null ? Optional.empty() : locationService.findById(assetReq.getLocation().getId());
        Optional<File> optionalImage = assetReq.getImage() == null ? Optional.empty() : fileService.findById(assetReq.getImage().getId());
        Optional<AssetCategory> optionalAssetCategory = assetReq.getCategory() == null ? Optional.empty() : assetCategoryService.findById(assetReq.getCategory().getId());
        Optional<Asset> optionalParentAsset = assetReq.getParentAsset() == null ? Optional.empty() : findById(assetReq.getParentAsset().getId());
        Optional<OwnUser> optionalUser = assetReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(assetReq.getPrimaryUser().getId());
        Optional<Deprecation> optionalDeprecation = assetReq.getDeprecation() == null ? Optional.empty() : deprecationService.findById(assetReq.getDeprecation().getId());

        //optional fields
        boolean second = assetReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = assetReq.getImage() == null || (optionalImage.isPresent() && optionalImage.get().getCompany().getId().equals(companyId));
        boolean fourth = assetReq.getCategory() == null || (optionalAssetCategory.isPresent() && optionalAssetCategory.get().getCompanySettings().getCompany().getId().equals(companyId));
        boolean fifth = assetReq.getParentAsset() == null || (optionalParentAsset.isPresent() && optionalParentAsset.get().getCompany().getId().equals(companyId));
        boolean sixth = assetReq.getPrimaryUser() == null || (optionalUser.isPresent() && optionalUser.get().getCompany().getId().equals(companyId));
        boolean seventh = assetReq.getDeprecation() == null || (optionalDeprecation.isPresent() && optionalDeprecation.get().getCompany().getId().equals(companyId));

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
}
