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
    private final DeprecationRepository DeprecationRepository;

    private final ModelMapper modelMapper;

    public Deprecation create(Deprecation Deprecation) {
        return DeprecationRepository.save(Deprecation);
    }

    public Deprecation update(Deprecation Deprecation) {
        return DeprecationRepository.save(Deprecation);
    }

    public Collection<Deprecation> getAll() { return DeprecationRepository.findAll(); }

    public void delete(Long id){ DeprecationRepository.deleteById(id);}

    public Optional<Deprecation> findById(Long id) {return DeprecationRepository.findById(id); }
}
