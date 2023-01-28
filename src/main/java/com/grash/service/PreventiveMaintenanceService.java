package com.grash.service;

import com.grash.dto.PreventiveMaintenancePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.PreventiveMaintenanceMapper;
import com.grash.model.OwnUser;
import com.grash.model.PreventiveMaintenance;
import com.grash.model.enums.RoleType;
import com.grash.repository.PreventiveMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    private final EntityManager em;

    private final PreventiveMaintenanceMapper preventiveMaintenanceMapper;

    @Transactional
    public PreventiveMaintenance create(PreventiveMaintenance PreventiveMaintenance) {
        PreventiveMaintenance savedPM = preventiveMaintenanceRepository.saveAndFlush(PreventiveMaintenance);
        em.refresh(savedPM);
        return savedPM;
    }

    @Transactional
    public PreventiveMaintenance update(Long id, PreventiveMaintenancePatchDTO preventiveMaintenance) {
        if (preventiveMaintenanceRepository.existsById(id)) {
            PreventiveMaintenance savedPreventiveMaintenance = preventiveMaintenanceRepository.findById(id).get();
            PreventiveMaintenance updatedPM = preventiveMaintenanceRepository.saveAndFlush(preventiveMaintenanceMapper.updatePreventiveMaintenance(savedPreventiveMaintenance, preventiveMaintenance));
            em.refresh(updatedPM);
            return updatedPM;
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

    public boolean hasAccess(OwnUser user, PreventiveMaintenance preventiveMaintenance) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(preventiveMaintenance.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, PreventiveMaintenance preventiveMaintenanceReq) {
        Long companyId = user.getCompany().getId();

        boolean first = companyService.isCompanyValid(preventiveMaintenanceReq.getCompany(), companyId);
        return first && canPatch(user, preventiveMaintenanceMapper.toPatchDto(preventiveMaintenanceReq));
    }

    public boolean canPatch(OwnUser user, PreventiveMaintenancePatchDTO preventiveMaintenanceReq) {
        Long companyId = user.getCompany().getId();
        boolean second = assetService.isAssetInCompany(preventiveMaintenanceReq.getAsset(), companyId, true);
        boolean third = teamService.isTeamInCompany(preventiveMaintenanceReq.getTeam(), companyId, true);
        boolean fourth = locationService.isLocationInCompany(preventiveMaintenanceReq.getLocation(), companyId, true);
        boolean fifth = userService.isUserInCompany(preventiveMaintenanceReq.getPrimaryUser(), companyId, true);
        return second && third && fourth && fifth;
    }

}
