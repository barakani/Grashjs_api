package com.grash.service;

import com.grash.model.Labor;
import com.grash.repository.LaborRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborService {
    private final LaborRepository LaborRepository;

    private final ModelMapper modelMapper;

    public Labor create(Labor Labor) {
        return LaborRepository.save(Labor);
    }

    public Labor update(Labor Labor) {
        return LaborRepository.save(Labor);
    }

    public Collection<Labor> getAll() { return LaborRepository.findAll(); }

    public void delete(Long id){ LaborRepository.deleteById(id);}

    public Optional<Labor> findById(Long id) {return LaborRepository.findById(id); }
}
