package com.grash.service;

import com.grash.dto.RolePatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.Relation;
import com.grash.model.Role;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final CompanySettingsService companySettingsService;

    public Role create(Role Role) {
        return roleRepository.save(Role);
    }

    public Role update(Long id, RolePatchDTO role) {
        if (roleRepository.existsById(id)) {
            Role savedRole = roleRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(role, savedRole);
            return roleRepository.save(savedRole);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Role> getAll() {
        return roleRepository.findAll();
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Collection<Role> findByCompany(Long id) {
        return roleRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Role role) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(role.getCompanySettings().getCompany().getId());
    }

    public boolean canCreate(User user, Role roleReq) {

        return canPatch(user, modelMapper.map(roleReq, RolePatchDTO.class));
    }

    public boolean canPatch(User user, RolePatchDTO roleReq) {
        Long companyId = user.getCompany().getId();

        Optional<CompanySettings> optionalCompanySettings = roleReq.getCompanySettings() == null ? Optional.empty() : companySettingsService.findById(roleReq.getCompanySettings().getId());

        boolean first = roleReq.getCompanySettings() == null || (optionalCompanySettings.isPresent() && optionalCompanySettings.get().getCompany().getId().equals(companyId));

        return first;
    }
}
