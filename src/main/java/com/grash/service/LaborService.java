package com.grash.service;

import com.grash.dto.LaborPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Labor;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.enums.RoleType;
import com.grash.repository.LaborRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborService {
    private final LaborRepository laborRepository;
    private final WorkOrderService workOrderService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public Labor create(Labor Labor) {
        return laborRepository.save(Labor);
    }

    public Labor update(Long id, LaborPatchDTO labor) {
        if (laborRepository.existsById(id)) {
            Labor savedLabor = laborRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(labor, savedLabor);
            return laborRepository.save(savedLabor);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Labor> getAll() {
        return laborRepository.findAll();
    }

    public void delete(Long id) {
        laborRepository.deleteById(id);
    }

    public Optional<Labor> findById(Long id) {
        return laborRepository.findById(id);
    }

    public boolean hasAccess(User user, Labor labor) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(labor.getCompany().getId());
    }

    public boolean canCreate(User user, Labor laborReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(laborReq.getCompany().getId());
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(laborReq.getWorkOrder().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalWorkOrder.isPresent() && optionalWorkOrder.get().getCompany().getId().equals(companyId);

        return first && second && canPatch(user, modelMapper.map(laborReq, LaborPatchDTO.class));
    }

    public boolean canPatch(User user, LaborPatchDTO laborReq) {
        Long companyId = user.getCompany().getId();

        Optional<User> optionalUser = laborReq.getWorker() == null ? Optional.empty() : userService.findById(laborReq.getWorker().getId());
        boolean first = !optionalUser.isPresent() || optionalUser.get().getCompany().getId().equals(companyId);

        if (first) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }
}
