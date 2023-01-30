package com.grash.controller;

import com.grash.advancedsearch.FilterField;
import com.grash.advancedsearch.SearchCriteria;
import com.grash.dto.SuccessResponse;
import com.grash.dto.WorkOrderPatchDTO;
import com.grash.dto.WorkOrderShowDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.WorkOrderMapper;
import com.grash.model.*;
import com.grash.model.enums.*;
import com.grash.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@RestController
@RequestMapping("/work-orders")
@Api(tags = "workOrder")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;
    private final WorkOrderMapper workOrderMapper;
    private final UserService userService;
    private final AssetService assetService;
    private final LocationService locationService;
    private final LaborService laborService;
    private final PartService partService;
    private final PartQuantityService partQuantityService;
    private final NotificationService notificationService;
    private final EmailService2 emailService2;
    private final TeamService teamService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @PostMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<WorkOrderShowDTO>> search(@RequestBody SearchCriteria searchCriteria, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            searchCriteria.filterCompany(user);
            if (user.getRole().getViewPermissions().contains(PermissionEntity.WORK_ORDERS)) {
                boolean canViewOthers = user.getRole().getViewOtherPermissions().contains(PermissionEntity.WORK_ORDERS);
                if (!canViewOthers) {
                    searchCriteria.getFilterFields().add(FilterField.builder()
                            .field("createdBy")
                            .value(user.getId())
                            .operation("eq")
                            .values(new ArrayList<>())
                            .alternatives(Arrays.asList(
                                    FilterField.builder()
                                            .field("assignedTo")
                                            .operation("inm")
                                            .joinType(JoinType.LEFT)
                                            .value("")
                                            .values(Collections.singletonList(user.getId())).build(),
                                    FilterField.builder()
                                            .field("primaryUser")
                                            .operation("eq")
                                            .value(user.getId())
                                            .values(Collections.singletonList(user.getId())).build(),
                                    FilterField.builder()
                                            .field("team")
                                            .operation("in")
                                            .value("")
                                            .values(teamService.findByUser(user.getId()).stream().map(Team::getId).collect(Collectors.toList())).build()
                            )).build());
                }
            } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(workOrderService.findBySearchCriteria(searchCriteria));
    }


    @GetMapping("/asset/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public Collection<WorkOrderShowDTO> getByAsset(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);
        if (optionalAsset.isPresent() && assetService.hasAccess(user, optionalAsset.get())) {
            return workOrderService.findByAsset(id).stream().map(workOrderMapper::toShowDto).collect(Collectors.toList());
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/location/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public Collection<WorkOrderShowDTO> getByLocation(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Location> optionalLocation = locationService.findById(id);
        if (optionalLocation.isPresent() && locationService.hasAccess(user, optionalLocation.get())) {
            return workOrderService.findByLocation(id).stream().map(workOrderMapper::toShowDto).collect(Collectors.toList());
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public WorkOrderShowDTO getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);
        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            if (workOrderService.hasAccess(user, savedWorkOrder) && user.getRole().getViewPermissions().contains(PermissionEntity.WORK_ORDERS) &&
                    (user.getRole().getViewOtherPermissions().contains(PermissionEntity.WORK_ORDERS) || savedWorkOrder.getCreatedBy().equals(user.getId()))) {
                return workOrderMapper.toShowDto(savedWorkOrder);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public WorkOrderShowDTO create(@ApiParam("WorkOrder") @Valid @RequestBody WorkOrder
                                           workOrderReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (workOrderService.canCreate(user, workOrderReq) && user.getRole().getCreatePermissions().contains(PermissionEntity.WORK_ORDERS)
                && (workOrderReq.getSignature() == null ||
                user.getCompany().getSubscription().getSubscriptionPlan().getFeatures().contains(PlanFeatures.SIGNATURE))) {
            if (user.getCompany().getCompanySettings().getGeneralPreferences().isAutoAssignWorkOrders()) {
                OwnUser primaryUser = workOrderReq.getPrimaryUser();
                workOrderReq.setPrimaryUser(primaryUser == null ? user : primaryUser);
            }
            WorkOrder createdWorkOrder = workOrderService.create(workOrderReq);
            if (createdWorkOrder.getAsset() != null) {
                Asset asset = assetService.findById(createdWorkOrder.getAsset().getId()).get();
                if (asset.getStatus().equals(AssetStatus.OPERATIONAL)) {
                    assetService.triggerDownTime(asset.getId());
                }
            }
            workOrderService.notify(createdWorkOrder);
            return workOrderMapper.toShowDto(createdWorkOrder);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/part/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrders for this part not found")})
    public Collection<WorkOrderShowDTO> getByPart(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Part> optionalPart = partService.findById(id);
        if (optionalPart.isPresent() && partService.hasAccess(user, optionalPart.get())) {
            Collection<PartQuantity> partQuantities = partQuantityService.findByPart(id).stream()
                    .filter(partQuantity -> partQuantity.getWorkOrder() != null).collect(Collectors.toList());
            Collection<WorkOrder> workOrders = partQuantities.stream().map(PartQuantity::getWorkOrder).collect(Collectors.toList());
            Collection<WorkOrder> uniqueWorkOrders = workOrders.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(WorkOrder::getId))),
                    ArrayList::new));
            return uniqueWorkOrders.stream().map(workOrderMapper::toShowDto).collect(Collectors.toList());
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public WorkOrderShowDTO patch(@ApiParam("WorkOrder") @Valid @RequestBody WorkOrderPatchDTO
                                          workOrder, @ApiParam("id") @PathVariable("id") Long id,
                                  HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);

        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            Status savedWorkOrderStatusBefore = savedWorkOrder.getStatus();
            if (workOrderService.hasAccess(user, savedWorkOrder) && workOrderService.canPatch(user, workOrder)
                    && savedWorkOrder.canBeEditedBy(user)
                    && (workOrder.getSignature() == null ||
                    user.getCompany().getSubscription().getSubscriptionPlan().getFeatures().contains(PlanFeatures.SIGNATURE))) {
                if (!workOrder.getStatus().equals(Status.IN_PROGRESS)) {
                    if (workOrder.getStatus().equals(Status.COMPLETE)) {
                        workOrder.setCompletedBy(user);
                        workOrder.setCompletedOn(new Date());
                        if (workOrder.getAsset() != null) {
                            Asset asset = workOrder.getAsset();
                            Collection<WorkOrder> workOrdersOfSameAsset = workOrderService.findByAsset(asset.getId());
                            if (workOrdersOfSameAsset.stream().noneMatch(workOrder1 -> !workOrder1.getId().equals(id) && !workOrder1.getStatus().equals(Status.COMPLETE))) {
                                assetService.stopDownTime(asset.getId());
                            }
                        }
                        Collection<Labor> primaryLabors = laborService.findByWorkOrder(id).stream().filter(Labor::isLogged).collect(Collectors.toList());
                        primaryLabors.forEach(laborService::stop);
                    }
                    Collection<Labor> labors = laborService.findByWorkOrder(id);
                    Collection<Labor> primaryTimes = labors.stream().filter(Labor::isLogged).collect(Collectors.toList());
                    primaryTimes.forEach(laborService::stop);
                }
                WorkOrder patchedWorkOrder = workOrderService.update(id, workOrder, user);
                if (user.getCompany().getCompanySettings().getGeneralPreferences().isWoUpdateForRequesters()
                        && savedWorkOrderStatusBefore != patchedWorkOrder.getStatus()
                        && patchedWorkOrder.getParentRequest() != null) {
                    String message = "The Work Order you requested, " + patchedWorkOrder.getTitle() + ", is now " + patchedWorkOrder.getStatus().getName();
                    Long requesterId = patchedWorkOrder.getParentRequest().getCreatedBy();
                    OwnUser requester = userService.findById(requesterId).get();
                    notificationService.create(new Notification(message, userService.findById(requesterId).get(), NotificationType.WORK_ORDER, id));
                    if (requester.getUserSettings().isEmailUpdatesForRequests()) {
                        Map<String, Object> mailVariables = new HashMap<String, Object>() {{
                            put("workOrderLink", frontendUrl + "/app/work-orders/" + id);
                            put("message", message);
                        }};
                        emailService2.sendMessageUsingThymeleafTemplate(new String[]{requester.getEmail()}, "Work Order Request Update", mailVariables, "requester-update.html");
                    }
                }
                boolean shouldNotify = !user.getCompany().getCompanySettings().getGeneralPreferences().isDisableClosedWorkOrdersNotif() || !patchedWorkOrder.getStatus().equals(Status.COMPLETE);
                if (shouldNotify) workOrderService.patchNotify(savedWorkOrder, patchedWorkOrder);
                return workOrderMapper.toShowDto(patchedWorkOrder);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrder not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);
        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            if (workOrderService.hasAccess(user, savedWorkOrder) && (
                    user.getId().equals(savedWorkOrder.getCreatedBy()) ||
                            user.getRole().getDeleteOtherPermissions().contains(PermissionEntity.WORK_ORDERS))) {
                workOrderService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrder not found", HttpStatus.NOT_FOUND);
    }

}
