package com.grash.service;

import com.grash.dto.AdditionalCostPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.AdditionalCostMapper;
import com.grash.model.AdditionalCost;
import com.grash.model.Company;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.AdditionalCostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalCostService {

    private final AdditionalCostRepository additionalCostRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    private final AdditionalCostMapper additionalCostMapper;

    public AdditionalCost create(AdditionalCost additionalCost) {
        return additionalCostRepository.save(additionalCost);
    }

    public AdditionalCost update(Long id, AdditionalCostPatchDTO additionalCost) {
        if (additionalCostRepository.existsById(id)) {
            AdditionalCost savedAdditionalCost = additionalCostRepository.findById(id).get();
            return additionalCostRepository.save(additionalCostMapper.updateAdditionalCost(savedAdditionalCost, additionalCost));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<AdditionalCost> getAll() {
        return additionalCostRepository.findAll();
    }

    public void delete(Long id) {
        additionalCostRepository.deleteById(id);
    }

    public Optional<AdditionalCost> findById(Long id) {
        return additionalCostRepository.findById(id);
    }

    public boolean hasAccess(User user, AdditionalCost additionalCost) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(additionalCost.getCompany().getId());
    }

    public boolean canCreate(User user, AdditionalCost additionalCostReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(additionalCostReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        if (first && canPatch(user, modelMapper.map(additionalCostReq, AdditionalCostPatchDTO.class))) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    public boolean canPatch(User user, AdditionalCostPatchDTO additionalCostReq) {
        Long companyId = user.getCompany().getId();
        Optional<User> optionalUser = additionalCostReq.getAssignedTo() == null ? Optional.empty() : userService.findById(additionalCostReq.getAssignedTo().getId());

        boolean first = !optionalUser.isPresent() || optionalUser.get().getCompany().getId().equals(companyId);
        
        if (first) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

}
