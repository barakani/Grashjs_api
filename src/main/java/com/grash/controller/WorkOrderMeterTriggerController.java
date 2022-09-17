package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.WorkOrderMeterTriggerPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.User;
import com.grash.model.WorkOrderMeterTrigger;
import com.grash.service.UserService;
import com.grash.service.WorkOrderMeterTriggerService;
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
@RequestMapping("/work-order-meter-triggers")
@Api(tags = "workOrderMeterTrigger")
@RequiredArgsConstructor
public class WorkOrderMeterTriggerController {

    private final WorkOrderMeterTriggerService workOrderMeterTriggerService;
    private final UserService userService;


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "WorkOrderMeterTrigger not found")})
    public WorkOrderMeterTrigger getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrderMeterTrigger> optionalWorkOrderMeterTrigger = workOrderMeterTriggerService.findById(id);
        if (optionalWorkOrderMeterTrigger.isPresent()) {
            WorkOrderMeterTrigger savedWorkOrderMeterTrigger = optionalWorkOrderMeterTrigger.get();
            if (workOrderMeterTriggerService.hasAccess(user, savedWorkOrderMeterTrigger)) {
                return optionalWorkOrderMeterTrigger.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public WorkOrderMeterTrigger create(@ApiParam("WorkOrderMeterTrigger") @RequestBody WorkOrderMeterTrigger workOrderMeterTriggerReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (workOrderMeterTriggerService.canCreate(user, workOrderMeterTriggerReq)) {
            return workOrderMeterTriggerService.create(workOrderMeterTriggerReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrderMeterTrigger not found")})
    public WorkOrderMeterTrigger patch(@ApiParam("WorkOrderMeterTrigger") @RequestBody WorkOrderMeterTriggerPatchDTO workOrderMeterTrigger, @ApiParam("id") @PathVariable("id") Long id,
                                       HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrderMeterTrigger> optionalWorkOrderMeterTrigger = workOrderMeterTriggerService.findById(id);

        if (optionalWorkOrderMeterTrigger.isPresent()) {
            WorkOrderMeterTrigger savedWorkOrderMeterTrigger = optionalWorkOrderMeterTrigger.get();
            if (workOrderMeterTriggerService.hasAccess(user, savedWorkOrderMeterTrigger) && workOrderMeterTriggerService.canPatch(user, workOrderMeterTrigger)) {
                return workOrderMeterTriggerService.update(id, workOrderMeterTrigger);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrderMeterTrigger not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "WorkOrderMeterTrigger not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<WorkOrderMeterTrigger> optionalWorkOrderMeterTrigger = workOrderMeterTriggerService.findById(id);
        if (optionalWorkOrderMeterTrigger.isPresent()) {
            WorkOrderMeterTrigger savedWorkOrderMeterTrigger = optionalWorkOrderMeterTrigger.get();
            if (workOrderMeterTriggerService.hasAccess(user, savedWorkOrderMeterTrigger)) {
                workOrderMeterTriggerService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("WorkOrderMeterTrigger not found", HttpStatus.NOT_FOUND);
    }

}
