package com.grash.service;

import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import com.grash.repository.CompanySettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanySettingsService {
    private final CompanySettingsRepository companySettingsRepository;

    public CompanySettings create(CompanySettings CompanySettings) {
        return companySettingsRepository.save(CompanySettings);
    }

    public CompanySettings update(CompanySettings CompanySettings) {
        return companySettingsRepository.save(CompanySettings);
    }

    public Page<CompanySettings> getAll(Pageable paging) {
        return companySettingsRepository.findAll(paging);
    }

    public void delete(Long id) {
        companySettingsRepository.deleteById(id);
    }

    public Optional<CompanySettings> findById(Long id) {
        return companySettingsRepository.findById(id);
    }

    public boolean hasAccess(User user, CompanySettings companySettings) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else if (user.getCompany().getCompanySettings().getId().equals(companySettings.getId())) {
            if (user.getRole().getPermissions().contains(BasicPermission.ACCESS_SETTINGS)) {
                return true;
            } else throw new CustomException("You don't have permission", HttpStatus.NOT_ACCEPTABLE);
        } else return false;
    }
}
