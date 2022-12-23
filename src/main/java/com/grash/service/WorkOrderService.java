package com.grash.service;

import com.grash.dto.WorkOrderPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.WorkOrderMapper;
import com.grash.model.*;
import com.grash.model.abstracts.WorkOrderBase;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.Priority;
import com.grash.model.enums.RoleType;
import com.grash.repository.WorkOrderHistoryRepository;
import com.grash.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderHistoryRepository workOrderHistoryRepository;
    private final LocationService locationService;
    private final TeamService teamService;
    private final AssetService assetService;
    private final UserService userService;
    private final CompanyService companyService;
    private final PurchaseOrderService purchaseOrderService;
    private final NotificationService notificationService;
    private final WorkOrderMapper workOrderMapper;
    private final EntityManager em;

    @Transactional
    public WorkOrder create(WorkOrder workOrder) {
        WorkOrder savedWorkOrder = workOrderRepository.saveAndFlush(workOrder);
        em.refresh(savedWorkOrder);
        return savedWorkOrder;
    }

    @Transactional
    public WorkOrder update(Long id, WorkOrderPatchDTO workOrder, OwnUser user) {
        if (workOrderRepository.existsById(id)) {
            WorkOrder savedWorkOrder = workOrderRepository.findById(id).get();
            WorkOrder updatedWorkOrder = workOrderRepository.saveAndFlush(workOrderMapper.updateWorkOrder(savedWorkOrder, workOrder));
            em.refresh(updatedWorkOrder);
            WorkOrderHistory workOrderHistory = WorkOrderHistory.builder()
                    .name("Updating " + workOrder.getTitle())
                    .workOrder(updatedWorkOrder)
                    .user(user)
                    .build();
            workOrderHistoryRepository.save(workOrderHistory);
            return updatedWorkOrder;
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

    public boolean hasAccess(OwnUser user, WorkOrder workOrder) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(workOrder.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, WorkOrder workOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(workOrderReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, workOrderMapper.toPatchDto(workOrderReq));
    }

    public boolean canPatch(OwnUser user, WorkOrderPatchDTO workOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = workOrderReq.getLocation() == null ? Optional.empty() : locationService.findById(workOrderReq.getLocation().getId());
        Optional<Team> optionalTeam = workOrderReq.getTeam() == null ? Optional.empty() : teamService.findById(workOrderReq.getTeam().getId());
        Optional<OwnUser> optionalUser = workOrderReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(workOrderReq.getPrimaryUser().getId());
        Optional<Asset> optionalAsset = workOrderReq.getAsset() == null ? Optional.empty() : assetService.findById(workOrderReq.getAsset().getId());

        boolean second = workOrderReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = workOrderReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));
        boolean fourth = workOrderReq.getPrimaryUser() == null || (optionalUser.isPresent() && optionalUser.get().getCompany().getId().equals(companyId));
        boolean fifth = workOrderReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));

        return second && third && fourth && fifth;
    }

    public void notify(WorkOrder workOrder) {

        String message = "WorkOrder " + workOrder.getTitle() + " has been assigned to you";
        workOrder.getUsers().forEach(user -> notificationService.create(new Notification(message, user, NotificationType.WORK_ORDER, workOrder.getId())));
    }

    public void patchNotify(WorkOrder oldWorkOrder, WorkOrder newWorkOrder) {
        String message = "WorkOrder " + newWorkOrder.getTitle() + " has been assigned to you";
        oldWorkOrder.getNewUsersToNotify(newWorkOrder.getUsers()).forEach(user -> notificationService.create(
                new Notification(message, user, NotificationType.WORK_ORDER, newWorkOrder.getId())));
    }

    public Collection<WorkOrder> findByAsset(Long id) {
        return workOrderRepository.findByAsset_Id(id);
    }

    public Collection<WorkOrder> findByPM(Long id) {
        return workOrderRepository.findByParentPreventiveMaintenance_Id(id);
    }

    public Collection<WorkOrder> findByLocation(Long id) {
        return workOrderRepository.findByLocation_Id(id);
    }

    public void save(WorkOrder workOrder) {
        workOrderRepository.save(workOrder);
    }

    public WorkOrder getWorkOrderFromWorkOrderBase(WorkOrderBase workOrderBase) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCompany(workOrderBase.getCompany());
        workOrder.setTitle(workOrderBase.getTitle());
        workOrder.setDescription(workOrderBase.getDescription());
        workOrder.setPriority(workOrderBase.getPriority());
        workOrder.setImage(workOrder.getImage());
        //TODO
        //workOrder.setFiles(workOrderBase.getFiles());
        workOrder.setAsset(workOrderBase.getAsset());
        workOrder.setLocation(workOrderBase.getLocation());
        workOrder.setPrimaryUser(workOrderBase.getPrimaryUser());
        workOrder.setTeam(workOrderBase.getTeam());
        return workOrder;
    }

    public Collection<WorkOrder> findByPrimaryUser(Long id) {
        return workOrderRepository.findByPrimaryUser_Id(id);
    }

    public Collection<WorkOrder> findByCompletedBy(Long id) {
        return workOrderRepository.findByCompletedBy_Id(id);
    }

    public Collection<WorkOrder> findByPriorityAndCompany(Priority priority, Long companyId) {
        return workOrderRepository.findByPriorityAndCompany_Id(priority, companyId);
    }

    public Collection<WorkOrder> findByCategory(Long id) {
        return workOrderRepository.findByCategory_Id(id);
    }
}
