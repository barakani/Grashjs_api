package com.grash.service;

import com.grash.model.AdditionalTime;
import com.grash.repository.AdditionalTimeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalTimeService {
    private final AdditionalTimeRepository AdditionalTimeRepository;

    private final ModelMapper modelMapper;

    public AdditionalTime create(AdditionalTime AdditionalTime) {
        return AdditionalTimeRepository.save(AdditionalTime);
    }

    public AdditionalTime update(AdditionalTime AdditionalTime) {
        return AdditionalTimeRepository.save(AdditionalTime);
    }

    public Collection<AdditionalTime> getAll() { return AdditionalTimeRepository.findAll(); }

    public void delete(Long id){ AdditionalTimeRepository.deleteById(id);}

    public Optional<AdditionalTime> findById(Long id) {return AdditionalTimeRepository.findById(id); }
}
