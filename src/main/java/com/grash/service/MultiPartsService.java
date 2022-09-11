package com.grash.service;

import com.grash.model.MultiParts;
import com.grash.repository.MultiPartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiPartsService {
    private final MultiPartsRepository MultiPartsRepository;

    public MultiParts create(MultiParts MultiParts) {
        return MultiPartsRepository.save(MultiParts);
    }

    public MultiParts update(MultiParts MultiParts) {
        return MultiPartsRepository.save(MultiParts);
    }

    public Collection<MultiParts> getAll() { return MultiPartsRepository.findAll(); }

    public void delete(Long id){ MultiPartsRepository.deleteById(id);}

    public Optional<MultiParts> findById(Long id) {return MultiPartsRepository.findById(id); }
}
