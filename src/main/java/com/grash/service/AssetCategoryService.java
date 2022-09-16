package com.grash.service;

import com.grash.dto.CategoryPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.AssetCategory;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.AssetCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetCategoryService {
    private final AssetCategoryRepository assetCategoryRepository;
    private final CompanySettingsService companySettingsService;

    private final ModelMapper modelMapper;

    public AssetCategory create(AssetCategory AssetCategory) {
        return assetCategoryRepository.save(AssetCategory);
    }

    public AssetCategory update(Long id, CategoryPatchDTO assetCategory) {
        if (assetCategoryRepository.existsById(id)) {
            AssetCategory savedAssetCategory = assetCategoryRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(assetCategory, savedAssetCategory);
            return assetCategoryRepository.save(savedAssetCategory);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<AssetCategory> getAll() {
        return assetCategoryRepository.findAll();
    }

    public void delete(Long id) {
        assetCategoryRepository.deleteById(id);
    }

    public Optional<AssetCategory> findById(Long id) {
        return assetCategoryRepository.findById(id);
    }

    public Collection<AssetCategory> findByCompanySettings(Long id) {
        return assetCategoryRepository.findByCompanySettings_Id(id);
    }

    public boolean hasAccess(User user, AssetCategory assetCategory) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(assetCategory.getCompanySettings().getCompany().getId());
    }

    public boolean canCreate(User user, AssetCategory assetCategoryReq) {
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(assetCategoryReq.getCompanySettings().getId());

        //Post only fields
        boolean first = optionalCompanySettings.isPresent() && optionalCompanySettings.get().getId().equals(
                user.getCompany().getCompanySettings().getId());

        if (first && canPatch(user, modelMapper.map(assetCategoryReq, CategoryPatchDTO.class))) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    public boolean canPatch(User user, CategoryPatchDTO assetCategoryReq) {
        return true;
    }
}
