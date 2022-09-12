package com.grash.service;

import com.grash.model.Asset;
import com.grash.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public Asset update(Asset Asset) {
        return assetRepository.save(Asset);
    }

    public Collection<Asset> getAll() { return assetRepository.findAll(); }

    public void delete(Long id){ assetRepository.deleteById(id);}

    public Optional<Asset> findById(Long id) {return assetRepository.findById(id); }
}
