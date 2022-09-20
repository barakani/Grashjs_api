package com.grash.service;

import com.grash.model.User;
import com.grash.model.UserSettings;
import com.grash.model.enums.RoleType;
import com.grash.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;

    public UserSettings create(UserSettings UserSettings) {
        return userSettingsRepository.save(UserSettings);
    }

    public UserSettings update(UserSettings UserSettings) {
        return userSettingsRepository.save(UserSettings);
    }

    public Collection<UserSettings> getAll() {
        return userSettingsRepository.findAll();
    }

    public void delete(Long id) {
        userSettingsRepository.deleteById(id);
    }

    public Optional<UserSettings> findById(Long id) {
        return userSettingsRepository.findById(id);
    }

    public boolean hasAccess(User user, UserSettings userSettings) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else
            return user.getUserSettings().getId().equals(userSettings.getId());
    }
}
