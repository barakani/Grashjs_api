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
    private final TimeCategoryRepository timeCategoryRepository;

    public TimeCategory create(TimeCategory TimeCategory) {
        return timeCategoryRepository.save(TimeCategory);
    }

    public TimeCategory update(TimeCategory TimeCategory) {
        return timeCategoryRepository.save(TimeCategory);
    }

    public Collection<TimeCategory> getAll() { return timeCategoryRepository.findAll(); }

    public void delete(Long id){ timeCategoryRepository.deleteById(id);}

    public Optional<TimeCategory> findById(Long id) {return timeCategoryRepository.findById(id); }
}
