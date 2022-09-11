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
    private final AssetCategoryRepository AssetCategoryRepository;

    private final ModelMapper modelMapper;

    public AssetCategory create(AssetCategory AssetCategory) {
        return AssetCategoryRepository.save(AssetCategory);
    }

    public AssetCategory update(AssetCategory AssetCategory) {
        return AssetCategoryRepository.save(AssetCategory);
    }

    public Collection<AssetCategory> getAll() { return AssetCategoryRepository.findAll(); }

    public void delete(Long id){ AssetCategoryRepository.deleteById(id);}

    public Optional<AssetCategory> findById(Long id) {return AssetCategoryRepository.findById(id); }
}
