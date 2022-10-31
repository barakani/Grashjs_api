package com.grash.controller;

import com.grash.advancedsearch.FilterField;
import com.grash.dto.SuccessResponse;
import com.grash.dto.WorkOrderPatchDTO;
import com.grash.dto.WorkOrderShowDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.WorkOrderMapper;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import com.grash.service.UserService;
import com.grash.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
// todo rename to 'api/v1/work-orders'
@RequestMapping("/work-orders")
@Api(tags = "workOrder")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;
    private final WorkOrderMapper workOrderMapper;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrderCategory not found")})
    public Collection<WorkOrderShowDTO> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return workOrderService.findByCompany(user.getCompany().getId()).stream().map(workOrderMapper::toShowDto).collect(Collectors.toList());
        } else return workOrderService.getAll().stream().map(workOrderMapper::toShowDto).collect(Collectors.toList());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<WorkOrder>> search(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                  //todo to be replaced by SearchCrireria
                                                  @RequestBody List<FilterField> filterFields) {
        return ResponseEntity.ok(workOrderService.findBySearchCriteria(filterFields, pageNum, pageSize));
    }


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public WorkOrderShowDTO getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);
        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            if (workOrderService.hasAccess(user, savedWorkOrder)) {
                return workOrderMapper.toShowDto(savedWorkOrder);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public WorkOrderShowDTO create(@ApiParam("WorkOrder") @Valid @RequestBody WorkOrder workOrderReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (workOrderService.canCreate(user, workOrderReq)) {
            WorkOrder createdWorkOrder = workOrderService.create(workOrderReq);
            workOrderService.notify(createdWorkOrder);
            return workOrderMapper.toShowDto(createdWorkOrder);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrder not found")})
    public WorkOrderShowDTO patch(@ApiParam("WorkOrder") @Valid @RequestBody WorkOrderPatchDTO workOrder, @ApiParam("id") @PathVariable("id") Long id,
                                  HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);

        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            if (workOrderService.hasAccess(user, savedWorkOrder) && workOrderService.canPatch(user, workOrder)) {
                WorkOrder patchedWorkOrder = workOrderService.update(id, workOrder);
                workOrderService.patchNotify(savedWorkOrder, patchedWorkOrder);
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
        User user = userService.whoami(req);

        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(id);
        if (optionalWorkOrder.isPresent()) {
            WorkOrder savedWorkOrder = optionalWorkOrder.get();
            if (workOrderService.hasAccess(user, savedWorkOrder) &&
                    user.getRole().getPermissions().contains(BasicPermission.DELETE_WORK_ORDERS)) {
                workOrderService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrder not found", HttpStatus.NOT_FOUND);
    }

}
