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
    private final RelationRepository RelationRepository;

    public Relation create(Relation Relation) {
        return RelationRepository.save(Relation);
    }

    public Relation update(Relation Relation) {
        return RelationRepository.save(Relation);
    }

    public Collection<Relation> getAll() { return RelationRepository.findAll(); }

    public void delete(Long id){ RelationRepository.deleteById(id);}

    public Optional<Relation> findById(Long id) {return RelationRepository.findById(id); }
}
