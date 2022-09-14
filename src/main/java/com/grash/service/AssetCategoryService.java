package com.grash.service;

import com.grash.dto.CategoryPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.AssetCategory;
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


}
