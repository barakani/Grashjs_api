package com.grash.service;

import com.grash.model.AssetCategory;
import com.grash.repository.AssetCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public AssetCategory update(AssetCategory AssetCategory) {
        return assetCategoryRepository.save(AssetCategory);
    }

    public Collection<AssetCategory> getAll() { return assetCategoryRepository.findAll(); }

    public void delete(Long id){ assetCategoryRepository.deleteById(id);}

    public Optional<AssetCategory> findById(Long id) {return assetCategoryRepository.findById(id); }
}
