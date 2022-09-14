package com.grash.service;

import com.grash.dto.AssetPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Asset;
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
}
