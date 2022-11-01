package com.grash.service;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.LaborCostMapper;
import com.grash.model.LaborCost;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.LaborCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborCostService {
    private final LaborCostRepository laborCostRepository;
    private final LaborCostMapper laborCostMapper;

    public LaborCost create(LaborCost LaborCost) {
        return laborCostRepository.save(LaborCost);
    }

    public LaborCost update(Long id, LaborCostPatchDTO laborCostDTO) {
        if (laborCostRepository.existsById(id)) {
            LaborCost savedLaborCost = laborCostRepository.findById(id).get();
            return laborCostRepository.save(laborCostMapper.updateLaborCost(savedLaborCost, laborCostDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<LaborCost> getAll() {
        return laborCostRepository.findAll();
    }

    public void delete(Long id) {
        laborCostRepository.deleteById(id);
    }

    public Optional<LaborCost> findById(Long id) {
        return laborCostRepository.findById(id);
    }
    
    public boolean hasAccess(OwnUser user, LaborCost laborCost) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(laborCost.getLabor().getCompany().getId());
    }

    public boolean canPatch(OwnUser user, LaborCostPatchDTO laborCostReq) {
        return true;
    }
}
