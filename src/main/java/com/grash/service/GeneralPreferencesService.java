package com.grash.service;

import com.grash.model.GeneralPreferences;
import com.grash.repository.GeneralPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralPreferencesService {
    private final GeneralPreferencesRepository generalPreferencesRepository;

    public GeneralPreferences create(GeneralPreferences GeneralPreferences) {
        return generalPreferencesRepository.save(GeneralPreferences);
    }

    public GeneralPreferences update(GeneralPreferences GeneralPreferences) {
        return generalPreferencesRepository.save(GeneralPreferences);
    }

    public Collection<GeneralPreferences> getAll() { return generalPreferencesRepository.findAll(); }

    public void delete(Long id){ generalPreferencesRepository.deleteById(id);}

    public Optional<GeneralPreferences> findById(Long id) {return generalPreferencesRepository.findById(id); }
}
