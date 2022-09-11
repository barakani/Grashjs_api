package com.grash.service;

import com.grash.model.Checklist;
import com.grash.repository.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository ChecklistRepository;

    private final ModelMapper modelMapper;

    public Checklist create(Checklist Checklist) {
        return ChecklistRepository.save(Checklist);
    }

    public Checklist update(Checklist Checklist) {
        return ChecklistRepository.save(Checklist);
    }

    public Collection<Checklist> getAll() { return ChecklistRepository.findAll(); }

    public void delete(Long id){ ChecklistRepository.deleteById(id);}

    public Optional<Checklist> findById(Long id) {return ChecklistRepository.findById(id); }
}
