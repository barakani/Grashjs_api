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
    private final GeneralPreferencesRepository GeneralPreferencesRepository;

    public GeneralPreferences create(GeneralPreferences GeneralPreferences) {
        return GeneralPreferencesRepository.save(GeneralPreferences);
    }

    public GeneralPreferences update(GeneralPreferences GeneralPreferences) {
        return GeneralPreferencesRepository.save(GeneralPreferences);
    }

    public Collection<GeneralPreferences> getAll() { return GeneralPreferencesRepository.findAll(); }

    public void delete(Long id){ GeneralPreferencesRepository.deleteById(id);}

    public Optional<GeneralPreferences> findById(Long id) {return GeneralPreferencesRepository.findById(id); }
}
