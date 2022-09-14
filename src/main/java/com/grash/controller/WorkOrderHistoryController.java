package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.WorkOrderHistory;
import com.grash.service.UserService;
import com.grash.service.WorkOrderHistoryService;
import com.grash.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/workOrderHistorys")
@Api(tags = "workOrderHistory")
@RequiredArgsConstructor
public class WorkOrderHistoryController {

    private final WorkOrderHistoryService workOrderHistoryService;
    private final UserService userService;
    private final WorkOrderService workOrderService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrderHistory not found")})
    public Optional<WorkOrderHistory> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrderHistory> optionalWorkOrderHistory = workOrderHistoryService.findById(id);
        if (optionalWorkOrderHistory.isPresent()) {
            WorkOrderHistory savedWorkOrderHistory = optionalWorkOrderHistory.get();
            if (hasAccess(user, savedWorkOrderHistory)) {
                return optionalWorkOrderHistory;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public WorkOrderHistory create(@ApiParam("WorkOrderHistory") @RequestBody WorkOrderHistory workOrderHistoryReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(workOrderHistoryReq.getWorkOrder().getId());
        if (optionalWorkOrder.isPresent()) {
            if (user.getCompany().getId().equals(optionalWorkOrder.get().getCompany().getId())) {
                return workOrderHistoryService.create(workOrderHistoryReq);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Invalid Work Order", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrderHistory not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<WorkOrderHistory> optionalWorkOrderHistory = workOrderHistoryService.findById(id);
        if (optionalWorkOrderHistory.isPresent()) {
            WorkOrderHistory savedWorkOrderHistory = optionalWorkOrderHistory.get();
            if (hasAccess(user, savedWorkOrderHistory)) {
                workOrderHistoryService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrderHistory not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, WorkOrderHistory workOrderHistory) {
        return user.getCompany().getId().equals(workOrderHistory.getWorkOrder().getCompany().getId());
    }
}
