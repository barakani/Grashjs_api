package com.grash.service;

import com.grash.dto.WorkOrderMeterTriggerPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Meter;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.WorkOrderMeterTrigger;
import com.grash.model.enums.RoleType;
import com.grash.repository.WorkOrderMeterTriggerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderMeterTriggerService {
    private final WorkOrderMeterTriggerRepository workOrderMeterTriggerRepository;
    private final WorkOrderService workOrderService;
    private final MeterService meterService;


    private final ModelMapper modelMapper;

    public WorkOrderMeterTrigger create(WorkOrderMeterTrigger WorkOrderMeterTrigger) {
        return workOrderMeterTriggerRepository.save(WorkOrderMeterTrigger);
    }

    public WorkOrderMeterTrigger update(Long id, WorkOrderMeterTriggerPatchDTO workOrderMeterTrigger) {
        if (workOrderMeterTriggerRepository.existsById(id)) {
            WorkOrderMeterTrigger savedWorkOrderMeterTrigger = workOrderMeterTriggerRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(workOrderMeterTrigger, savedWorkOrderMeterTrigger);
            return workOrderMeterTriggerRepository.save(savedWorkOrderMeterTrigger);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<WorkOrderMeterTrigger> getAll() {
        return workOrderMeterTriggerRepository.findAll();
    }

    public void delete(Long id) {
        workOrderMeterTriggerRepository.deleteById(id);
    }

    public Optional<WorkOrderMeterTrigger> findById(Long id) {
        return workOrderMeterTriggerRepository.findById(id);
    }

    public boolean hasAccess(User user, WorkOrderMeterTrigger workOrderMeterTrigger) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(workOrderMeterTrigger.getWorkOrder().getCompany().getId());
    }

    public boolean canCreate(User user, WorkOrderMeterTrigger workOrderMeterTriggerReq) {
        Long companyId = user.getCompany().getId();

        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(workOrderMeterTriggerReq.getWorkOrder().getId());
        Optional<Meter> optionalMeter = meterService.findById(workOrderMeterTriggerReq.getMeter().getId());

        //@NotNull fields
        boolean first = optionalWorkOrder.isPresent() && optionalWorkOrder.get().getCompany().getId().equals(companyId);
        boolean second = optionalMeter.isPresent() && optionalMeter.get().getCompany().getId().equals(companyId);

        return first && second && canPatch(user, modelMapper.map(workOrderMeterTriggerReq, WorkOrderMeterTriggerPatchDTO.class));
    }

    public boolean canPatch(User user, WorkOrderMeterTriggerPatchDTO workOrderMeterTriggerReq) {
        return true;
    }
}
