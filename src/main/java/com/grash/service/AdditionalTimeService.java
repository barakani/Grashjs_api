package com.grash.service;

import com.grash.dto.AdditionalTimePatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.AdditionalTimeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalTimeService {
    private final AdditionalTimeRepository additionalTimeRepository;

    private final ModelMapper modelMapper;
    private final CompanyService companyService;
    private final TimeCategoryService timeCategoryService;
    private final UserService userService;
    private final WorkOrderService workOrderService;

    public AdditionalTime create(AdditionalTime AdditionalTime) {
        return additionalTimeRepository.save(AdditionalTime);
    }

    public AdditionalTime update(Long id, AdditionalTimePatchDTO additionalTime) {
        if (additionalTimeRepository.existsById(id)) {
            AdditionalTime savedAdditionalTime = additionalTimeRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(additionalTime, savedAdditionalTime);
            return additionalTimeRepository.save(savedAdditionalTime);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<AdditionalTime> getAll() {
        return additionalTimeRepository.findAll();
    }

    public void delete(Long id) {
        additionalTimeRepository.deleteById(id);
    }

    public Optional<AdditionalTime> findById(Long id) {
        return additionalTimeRepository.findById(id);
    }

    public boolean hasAccess(User user, AdditionalTime additionalTime) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(additionalTime.getCompany().getId());
    }

    public boolean canCreate(User user, AdditionalTime additionalTimeReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(additionalTimeReq.getCompany().getId());
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(additionalTimeReq.getWorkOrder().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalWorkOrder.isPresent() && optionalWorkOrder.get().getCompany().getId().equals(companyId);

        if (first && second && canPatch(user, modelMapper.map(additionalTimeReq, AdditionalTimePatchDTO.class))) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    public boolean canPatch(User user, AdditionalTimePatchDTO additionalTimeReq) {
        Long companyId = user.getCompany().getId();

        Optional<TimeCategory> optionalTimeCategory = additionalTimeReq.getTimeCategory() == null ? Optional.empty() : timeCategoryService.findById(additionalTimeReq.getTimeCategory().getId());
        Optional<User> optionalUser = additionalTimeReq.getAssignedTo() == null ? Optional.empty() : userService.findById(additionalTimeReq.getAssignedTo().getId());

        //optional fields
        boolean third = !optionalTimeCategory.isPresent() || optionalTimeCategory.get().getCompanySettings().getCompany().getId().equals(companyId);
        boolean sixth = !optionalUser.isPresent() || optionalUser.get().getCompany().getId().equals(companyId);

        if (third && sixth) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }
}
