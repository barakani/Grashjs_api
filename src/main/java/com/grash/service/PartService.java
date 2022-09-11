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
    private final PartRepository PartRepository;

    public Part create(Part Part) {
        return PartRepository.save(Part);
    }

    public Part update(Part Part) {
        return PartRepository.save(Part);
    }

    public Collection<Part> getAll() { return PartRepository.findAll(); }

    public void delete(Long id){ PartRepository.deleteById(id);}

    public Optional<Part> findById(Long id) {return PartRepository.findById(id); }
}
