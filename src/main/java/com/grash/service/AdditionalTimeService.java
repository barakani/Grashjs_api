package com.grash.service;

import com.grash.exception.CustomException;
import com.grash.model.AdditionalTime;
import com.grash.repository.AdditionalTimeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalTimeService {
    private final AdditionalTimeRepository additionalTimeRepository;

    private final ModelMapper modelMapper;

    public AdditionalTime create(AdditionalTime AdditionalTime) {
        return additionalTimeRepository.save(AdditionalTime);
    }

    public AdditionalTime update(Long id, AdditionalTime additionalTime) {
        if (additionalTimeRepository.existsById(id)) {
            return additionalTimeRepository.save(additionalTime);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<AdditionalTime> getAll() { return additionalTimeRepository.findAll(); }

    public void delete(Long id){ additionalTimeRepository.deleteById(id);}

    public Optional<AdditionalTime> findById(Long id) {return additionalTimeRepository.findById(id); }
}
