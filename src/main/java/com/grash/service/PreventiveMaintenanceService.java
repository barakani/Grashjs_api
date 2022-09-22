package com.grash.service;

import com.grash.dto.PreventiveMaintenancePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.PreventiveMaintenanceMapper;
import com.grash.model.*;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.PreventiveMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreventiveMaintenanceService {
    private final PreventiveMaintenanceRepository preventiveMaintenanceRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final AssetService assetService;
    private final CompanyService companyService;
    private final NotificationService notificationService;
    private final LocationService locationService;

    private final PreventiveMaintenanceMapper preventiveMaintenanceMapper;

    public PreventiveMaintenance create(PreventiveMaintenance PreventiveMaintenance) {
        return preventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public PreventiveMaintenance update(Long id, PreventiveMaintenancePatchDTO preventiveMaintenance) {
        if (preventiveMaintenanceRepository.existsById(id)) {
            PreventiveMaintenance savedPreventiveMaintenance = preventiveMaintenanceRepository.findById(id).get();
            return preventiveMaintenanceRepository.save(preventiveMaintenanceMapper.updatePreventiveMaintenance(savedPreventiveMaintenance, preventiveMaintenance));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<PreventiveMaintenance> getAll() {
        return preventiveMaintenanceRepository.findAll();
    }

    public void delete(Long id) {
        preventiveMaintenanceRepository.deleteById(id);
    }

    public Optional<PreventiveMaintenance> findById(Long id) {
        return preventiveMaintenanceRepository.findById(id);
    }

    public Collection<PreventiveMaintenance> findByCompany(Long id) {
        return preventiveMaintenanceRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, PreventiveMaintenance preventiveMaintenance) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(preventiveMaintenance.getCompany().getId());
    }

    public boolean canCreate(User user, PreventiveMaintenance preventiveMaintenanceReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(preventiveMaintenanceReq.getCompany().getId());
        Optional<Asset> optionalAsset = assetService.findById(preventiveMaintenanceReq.getAsset().getId());
        Optional<Location> optionalLocation = locationService.findById(preventiveMaintenanceReq.getLocation().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId);
        boolean third = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);

        return first && second && third && canPatch(user, preventiveMaintenanceMapper.toDto(preventiveMaintenanceReq));
    }

    public boolean canPatch(User user, PreventiveMaintenancePatchDTO preventiveMaintenanceReq) {
        Long companyId = user.getCompany().getId();

        Optional<Asset> optionalAsset = preventiveMaintenanceReq.getAsset() == null ? Optional.empty() : assetService.findById(preventiveMaintenanceReq.getAsset().getId());
        Optional<Team> optionalTeam = preventiveMaintenanceReq.getTeam() == null ? Optional.empty() : teamService.findById(preventiveMaintenanceReq.getTeam().getId());
        Optional<Location> optionalLocation = preventiveMaintenanceReq.getLocation() == null ? Optional.empty() : locationService.findById(preventiveMaintenanceReq.getLocation().getId());
        Optional<User> optionalPrimaryUser = preventiveMaintenanceReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(preventiveMaintenanceReq.getPrimaryUser().getId());

        boolean second = preventiveMaintenanceReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        boolean third = preventiveMaintenanceReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));
        boolean fourth = preventiveMaintenanceReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean fifth = preventiveMaintenanceReq.getPrimaryUser() == null || (optionalPrimaryUser.isPresent() && optionalPrimaryUser.get().getCompany().getId().equals(companyId));

        return second && third && fourth && fifth;
    }

    public void notify(PreventiveMaintenance preventiveMaintenance) {

        String message = "PreventiveMaintenance " + preventiveMaintenance.getTitle() + " has been assigned to you";
        if (preventiveMaintenance.getPrimaryUser() != null) {
            notificationService.create(new Notification(message, preventiveMaintenance.getPrimaryUser(), NotificationType.PREVENTIVE_MAINTENANCE, preventiveMaintenance.getId()));
        }
        if (preventiveMaintenance.getAssignedTo() != null) {
            preventiveMaintenance.getAssignedTo().forEach(assignedUser ->
                    notificationService.create(new Notification(message, assignedUser, NotificationType.PREVENTIVE_MAINTENANCE, preventiveMaintenance.getId())));
        }
        if (preventiveMaintenance.getTeam() != null) {
            preventiveMaintenance.getTeam().getUsers().forEach(user ->
                    notificationService.create(new Notification(message, user, NotificationType.PREVENTIVE_MAINTENANCE, preventiveMaintenance.getId())));
        }
    }

    public void patchNotify(PreventiveMaintenance oldPreventiveMaintenance, PreventiveMaintenance newPreventiveMaintenance) {
        String message = "PreventiveMaintenance " + newPreventiveMaintenance.getTitle() + " has been assigned to you";
        if (newPreventiveMaintenance.getPrimaryUser() != null && !newPreventiveMaintenance.getPrimaryUser().getId().equals(oldPreventiveMaintenance.getPrimaryUser().getId())) {
            notificationService.create(new Notification(message, newPreventiveMaintenance.getPrimaryUser(), NotificationType.PREVENTIVE_MAINTENANCE, newPreventiveMaintenance.getId()));
        }
        if (newPreventiveMaintenance.getAssignedTo() != null) {
            List<User> newUsers = newPreventiveMaintenance.getAssignedTo().stream().filter(
                    user -> oldPreventiveMaintenance.getAssignedTo().stream().noneMatch(user1 -> user1.getId().equals(user.getId()))).collect(Collectors.toList());
            newUsers.forEach(newUser ->
                    notificationService.create(new Notification(message, newUser, NotificationType.PREVENTIVE_MAINTENANCE, newPreventiveMaintenance.getId())));
        }
        if (newPreventiveMaintenance.getTeam() != null && !newPreventiveMaintenance.getTeam().getId().equals(oldPreventiveMaintenance.getTeam().getId())) {
            newPreventiveMaintenance.getTeam().getUsers().forEach(user ->
                    notificationService.create(new Notification(message, user, NotificationType.PREVENTIVE_MAINTENANCE, newPreventiveMaintenance.getId())));
        }
    }
}
