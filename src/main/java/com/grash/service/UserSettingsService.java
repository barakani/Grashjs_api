package com.grash.service;

import com.grash.model.UserSettings;
import com.grash.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepository UserSettingsRepository;

    public UserSettings create(UserSettings UserSettings) {
        return UserSettingsRepository.save(UserSettings);
    }

    public UserSettings update(UserSettings UserSettings) {
        return UserSettingsRepository.save(UserSettings);
    }

    public Collection<UserSettings> getAll() { return UserSettingsRepository.findAll(); }

    public void delete(Long id){ UserSettingsRepository.deleteById(id);}

    public Optional<UserSettings> findById(Long id) {return UserSettingsRepository.findById(id); }
}
