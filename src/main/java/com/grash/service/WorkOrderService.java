package com.grash.service;

import com.grash.dto.WorkOrderPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final LocationService locationService;
    private final TeamService teamService;
    private final AssetService assetService;
    private final UserService userService;
    private final CompanyService companyService;
    private final PurchaseOrderService purchaseOrderService;
    private final ModelMapper modelMapper;

    public WorkOrder create(WorkOrder WorkOrder) {
        return workOrderRepository.save(WorkOrder);
    }

    public WorkOrder update(Long id, WorkOrderPatchDTO workOrder) {
        if (workOrderRepository.existsById(id)) {
            WorkOrder savedWorkOrder = workOrderRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(workOrder, savedWorkOrder);
            return workOrderRepository.save(savedWorkOrder);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<WorkOrder> getAll() {
        return workOrderRepository.findAll();
    }

    public void delete(Long id) {
        workOrderRepository.deleteById(id);
    }

    public Optional<WorkOrder> findById(Long id) {
        return workOrderRepository.findById(id);
    }

    public Collection<WorkOrder> findByCompany(Long id) {
        return workOrderRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, WorkOrder workOrder) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(workOrder.getCompany().getId());
    }

    public boolean canCreate(User user, WorkOrder workOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(workOrderReq.getCompany().getId());
        Optional<Location> optionalLocation = locationService.findById(workOrderReq.getLocation().getId());
        Optional<Asset> optionalAsset = assetService.findById(workOrderReq.getAsset().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);
        boolean third = optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId);

        return first && second && third && canPatch(user, modelMapper.map(workOrderReq, WorkOrderPatchDTO.class));
    }

    public boolean canPatch(User user, WorkOrderPatchDTO workOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = workOrderReq.getLocation() == null ? Optional.empty() : locationService.findById(workOrderReq.getLocation().getId());
        Optional<Team> optionalTeam = workOrderReq.getTeam() == null ? Optional.empty() : teamService.findById(workOrderReq.getTeam().getId());
        Optional<User> optionalUser = workOrderReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(workOrderReq.getPrimaryUser().getId());
        Optional<Asset> optionalAsset = workOrderReq.getAsset() == null ? Optional.empty() : assetService.findById(workOrderReq.getAsset().getId());
        Optional<PurchaseOrder> optionalPurchaseOrder = workOrderReq.getPurchaseOrder() == null ? Optional.empty() : purchaseOrderService.findById(workOrderReq.getPurchaseOrder().getId());

        boolean second = workOrderReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = workOrderReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));
        boolean fourth = workOrderReq.getPrimaryUser() == null || (optionalUser.isPresent() && optionalUser.get().getCompany().getId().equals(companyId));
        boolean fifth = workOrderReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        boolean sixth = workOrderReq.getPurchaseOrder() == null || (optionalPurchaseOrder.isPresent() && optionalPurchaseOrder.get().getCompany().getId().equals(companyId));

        return second && third && fourth && fifth && sixth;
    }
}
