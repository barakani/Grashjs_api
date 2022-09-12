package com.grash.service;

import com.grash.model.Deprecation;
import com.grash.repository.DeprecationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeprecationService {
    private final DeprecationRepository deprecationRepository;

    private final ModelMapper modelMapper;

    public Deprecation create(Deprecation Deprecation) {
        return deprecationRepository.save(Deprecation);
    }

    public Deprecation update(Deprecation Deprecation) {
        return deprecationRepository.save(Deprecation);
    }

    public Collection<Deprecation> getAll() { return deprecationRepository.findAll(); }

    public void delete(Long id){ deprecationRepository.deleteById(id);}

    public Optional<Deprecation> findById(Long id) {return deprecationRepository.findById(id); }
}
