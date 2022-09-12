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
    private final LaborRepository laborRepository;

    private final ModelMapper modelMapper;

    public Labor create(Labor Labor) {
        return laborRepository.save(Labor);
    }

    public Labor update(Labor Labor) {
        return laborRepository.save(Labor);
    }

    public Collection<Labor> getAll() { return laborRepository.findAll(); }

    public void delete(Long id){ laborRepository.deleteById(id);}

    public Optional<Labor> findById(Long id) {return laborRepository.findById(id); }
}
