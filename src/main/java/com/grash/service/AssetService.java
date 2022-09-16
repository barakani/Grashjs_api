package com.grash.service;

import com.grash.dto.AssetPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final LocationService locationService;
    private final ImageService imageService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final UserService userService;
    private final CompanyService companyService;


    private final ModelMapper modelMapper;

    public Asset create(Asset Asset) {
        return assetRepository.save(Asset);
    }

    public Asset update(Long id, AssetPatchDTO asset) {
        if (assetRepository.existsById(id)) {
            Asset savedAsset = assetRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(asset, savedAsset);
            return assetRepository.save(savedAsset);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
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

    public boolean hasAccess(User user, Asset asset) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(asset.getCompany().getId());
    }

    public boolean canCreate(User user, Asset assetReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(assetReq.getCompany().getId());
        Optional<Location> optionalLocation = locationService.findById(assetReq.getLocation().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);

        return first && second && canPatch(user, modelMapper.map(assetReq, AssetPatchDTO.class));
    }

    public boolean canPatch(User user, AssetPatchDTO assetReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = assetReq.getLocation() == null ? Optional.empty() : locationService.findById(assetReq.getLocation().getId());
        Optional<Image> optionalImage = assetReq.getImage() == null ? Optional.empty() : imageService.findById(assetReq.getImage().getId());
        Optional<AssetCategory> optionalAssetCategory = assetReq.getCategory() == null ? Optional.empty() : assetCategoryService.findById(assetReq.getCategory().getId());
        Optional<Asset> optionalParentAsset = assetReq.getParentAsset() == null ? Optional.empty() : findById(assetReq.getParentAsset().getId());
        Optional<User> optionalUser = assetReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(assetReq.getPrimaryUser().getId());
        Optional<Deprecation> optionalDeprecation = assetReq.getDeprecation() == null ? Optional.empty() : deprecationService.findById(assetReq.getDeprecation().getId());

        //optional fields
        boolean second = !optionalLocation.isPresent() || optionalLocation.get().getCompany().getId().equals(companyId);
        boolean third = !optionalImage.isPresent() || optionalImage.get().getCompany().getId().equals(companyId);
        boolean fourth = !optionalAssetCategory.isPresent() || optionalAssetCategory.get().getCompanySettings().getCompany().getId().equals(companyId);
        boolean fifth = !optionalParentAsset.isPresent() || optionalParentAsset.get().getCompany().getId().equals(companyId);
        boolean sixth = !optionalUser.isPresent() || optionalUser.get().getCompany().getId().equals(companyId);
        boolean seventh = !optionalDeprecation.isPresent() || optionalDeprecation.get().getCompany().getId().equals(companyId);

        if (second && third && fourth && fifth && sixth && seventh) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }
}
