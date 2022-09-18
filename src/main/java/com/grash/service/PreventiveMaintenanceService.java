package com.grash.service;

import com.grash.dto.PreventiveMaintenancePatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.PreventiveMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreventiveMaintenanceService {
    private final PreventiveMaintenanceRepository preventiveMaintenanceRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final AssetService assetService;
    private final CompanyService companyService;
    private final LocationService locationService;

    private final ModelMapper modelMapper;

    public PreventiveMaintenance create(PreventiveMaintenance PreventiveMaintenance) {
        return preventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public PreventiveMaintenance update(Long id, PreventiveMaintenancePatchDTO preventiveMaintenance) {
        if (preventiveMaintenanceRepository.existsById(id)) {
            PreventiveMaintenance savedPreventiveMaintenance = preventiveMaintenanceRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(preventiveMaintenance, savedPreventiveMaintenance);
            return preventiveMaintenanceRepository.save(savedPreventiveMaintenance);
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

        return first && second && third && canPatch(user, modelMapper.map(preventiveMaintenanceReq, PreventiveMaintenancePatchDTO.class));
    }

    public boolean canPatch(User user, PreventiveMaintenancePatchDTO preventiveMaintenanceReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = preventiveMaintenanceReq.getCompany() == null ? Optional.empty() : companyService.findById(preventiveMaintenanceReq.getCompany().getId());
        Optional<Asset> optionalAsset = preventiveMaintenanceReq.getAsset() == null ? Optional.empty() :assetService.findById(preventiveMaintenanceReq.getAsset().getId());
        Optional<Team> optionalTeam = preventiveMaintenanceReq.getTeam() == null ? Optional.empty() : teamService.findById(preventiveMaintenanceReq.getTeam().getId());
        Optional<Location> optionalLocation = preventiveMaintenanceReq.getLocation() == null ? Optional.empty() : locationService.findById(preventiveMaintenanceReq.getLocation().getId());
        Optional<User> optionalPrimaryUser = preventiveMaintenanceReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(preventiveMaintenanceReq.getPrimaryUser().getId());

        boolean first = preventiveMaintenanceReq.getCompany() ==  null || (optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId));
        boolean second = preventiveMaintenanceReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getId().equals(companyId));
        boolean third = preventiveMaintenanceReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));
        boolean fourth = preventiveMaintenanceReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean fifth = preventiveMaintenanceReq.getPrimaryUser() == null || (optionalPrimaryUser.isPresent() && optionalPrimaryUser.get().getCompany().getId().equals(companyId));

        return first && second && third && fourth && fifth;
    }
}
