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
    private final MultiPartsRepository multiPartsRepository;

    public MultiParts create(MultiParts MultiParts) {
        return multiPartsRepository.save(MultiParts);
    }

    public MultiParts update(MultiParts MultiParts) {
        return multiPartsRepository.save(MultiParts);
    }

    public Collection<MultiParts> getAll() { return multiPartsRepository.findAll(); }

    public void delete(Long id){ multiPartsRepository.deleteById(id);}

    public Optional<MultiParts> findById(Long id) {return multiPartsRepository.findById(id); }
}
