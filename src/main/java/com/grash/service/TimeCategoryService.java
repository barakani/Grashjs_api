package com.grash.service;

import com.grash.model.TimeCategory;
import com.grash.repository.TimeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCategoryService {
    private final TimeCategoryRepository TimeCategoryRepository;

    public TimeCategory create(TimeCategory TimeCategory) {
        return TimeCategoryRepository.save(TimeCategory);
    }

    public TimeCategory update(TimeCategory TimeCategory) {
        return TimeCategoryRepository.save(TimeCategory);
    }

    public Collection<TimeCategory> getAll() { return TimeCategoryRepository.findAll(); }

    public void delete(Long id){ TimeCategoryRepository.deleteById(id);}

    public Optional<TimeCategory> findById(Long id) {return TimeCategoryRepository.findById(id); }
}
