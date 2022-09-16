package com.grash.service;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.LaborCost;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.LaborCostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborCostService {
    private final LaborCostRepository laborCostRepository;
    private final ModelMapper modelMapper;
    private final CompanyService companyService;

    public LaborCost create(LaborCost LaborCost) {
        return laborCostRepository.save(LaborCost);
    }

    public LaborCost update(Long id, LaborCostPatchDTO laborCostDTO) {
        if (laborCostRepository.existsById(id)) {
            LaborCost savedLaborCost = laborCostRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(laborCostDTO, savedLaborCost);
            return laborCostRepository.save(savedLaborCost);
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

    public Collection<LaborCost> findByCompany(Long id) {
        return laborCostRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, LaborCost laborCost) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(laborCost.getCompany().getId());
    }

    public boolean canCreate(User user, LaborCost laborCostReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(laborCostReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(laborCostReq, LaborCostPatchDTO.class));
    }

    public boolean canPatch(User user, LaborCostPatchDTO laborCostReq) {
        return true;
    }
}
