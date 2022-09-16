package com.grash.service;

import com.grash.dto.DeprecationPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Deprecation;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.DeprecationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeprecationService {
    private final DeprecationRepository deprecationRepository;
    private final CompanyService companyService;

    private final ModelMapper modelMapper;

    public Deprecation create(Deprecation Deprecation) {
        return deprecationRepository.save(Deprecation);
    }

    public Deprecation update(Long id, DeprecationPatchDTO deprecation) {
        if (deprecationRepository.existsById(id)) {
            Deprecation savedDeprecation = deprecationRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(deprecation, savedDeprecation);
            return deprecationRepository.save(savedDeprecation);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Deprecation> getAll() {
        return deprecationRepository.findAll();
    }

    public void delete(Long id) {
        deprecationRepository.deleteById(id);
    }

    public Optional<Deprecation> findById(Long id) {
        return deprecationRepository.findById(id);
    }

    public boolean hasAccess(User user, Deprecation deprecation) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(deprecation.getCompany().getId());
    }

    public boolean canCreate(User user, Deprecation deprecationReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(deprecationReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(deprecationReq, DeprecationPatchDTO.class));
    }

    public boolean canPatch(User user, DeprecationPatchDTO deprecationReq) {
        return true;
    }
}
