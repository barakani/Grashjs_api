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
    private final AssetRepository AssetRepository;

    private final ModelMapper modelMapper;

    public Asset create(Asset Asset) {
        return AssetRepository.save(Asset);
    }

    public Asset update(Asset Asset) {
        return AssetRepository.save(Asset);
    }

    public Collection<Asset> getAll() { return AssetRepository.findAll(); }

    public void delete(Long id){ AssetRepository.deleteById(id);}

    public Optional<Asset> findById(Long id) {return AssetRepository.findById(id); }
}
