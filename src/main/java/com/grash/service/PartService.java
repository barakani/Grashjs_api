package com.grash.service;

import com.grash.model.Part;
import com.grash.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public Part create(Part Part) {
        return partRepository.save(Part);
    }

    public Part update(Part Part) {
        return partRepository.save(Part);
    }

    public Collection<Part> getAll() { return partRepository.findAll(); }

    public void delete(Long id){ partRepository.deleteById(id);}

    public Optional<Part> findById(Long id) {return partRepository.findById(id); }
}
