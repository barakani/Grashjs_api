package com.grash.service;

import com.grash.model.OwnUser;
import com.grash.model.WorkOrderConfiguration;
import com.grash.model.enums.RoleType;
import com.grash.repository.WorkOrderConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderConfigurationService {
    private final WorkOrderConfigurationRepository workOrderConfigurationRepository;

    public WorkOrderConfiguration create(WorkOrderConfiguration workOrderConfiguration) {
        return workOrderConfigurationRepository.save(workOrderConfiguration);
    }

    public WorkOrderConfiguration update(WorkOrderConfiguration WorkOrderConfiguration) {
        return workOrderConfigurationRepository.save(WorkOrderConfiguration);
    }

    public Collection<WorkOrderConfiguration> getAll() {
        return workOrderConfigurationRepository.findAll();
    }

    public void delete(Long id) {
        workOrderConfigurationRepository.deleteById(id);
    }

    public Optional<WorkOrderConfiguration> findById(Long id) {
        return workOrderConfigurationRepository.findById(id);
    }

    public boolean hasAccess(OwnUser user, WorkOrderConfiguration workOrderConfiguration) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else
            return user.getCompany().getId().equals(workOrderConfiguration.getCompanySettings().getCompany().getId());
    }
}
