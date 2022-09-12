package com.grash.service;

import com.grash.model.Relation;
import com.grash.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {
    private final RelationRepository relationRepository;

    public Relation create(Relation Relation) {
        return relationRepository.save(Relation);
    }

    public Relation update(Relation Relation) {
        return relationRepository.save(Relation);
    }

    public Collection<Relation> getAll() { return relationRepository.findAll(); }

    public void delete(Long id){ relationRepository.deleteById(id);}

    public Optional<Relation> findById(Long id) {return relationRepository.findById(id); }
}
