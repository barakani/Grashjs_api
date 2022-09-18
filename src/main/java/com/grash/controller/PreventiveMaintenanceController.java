package com.grash.controller;

import com.grash.dto.PreventiveMaintenancePatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.PreventiveMaintenance;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.PreventiveMaintenanceService;
import com.grash.service.UserService;
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
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/preventiveMaintenances")
@Api(tags = "preventiveMaintenance")
@RequiredArgsConstructor
public class PreventiveMaintenanceController {

    private final PreventiveMaintenanceService preventiveMaintenanceService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "PreventiveMaintenanceCategory not found")})
    public Collection<PreventiveMaintenance> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return preventiveMaintenanceService.findByCompany(user.getCompany().getId());
        } else return preventiveMaintenanceService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "PreventiveMaintenance not found")})
    public PreventiveMaintenance getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<PreventiveMaintenance> optionalPreventiveMaintenance = preventiveMaintenanceService.findById(id);
        if (optionalPreventiveMaintenance.isPresent()) {
            PreventiveMaintenance savedPreventiveMaintenance = optionalPreventiveMaintenance.get();
            if (preventiveMaintenanceService.hasAccess(user, savedPreventiveMaintenance)) {
                return optionalPreventiveMaintenance.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasPreventiveMaintenance('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public PreventiveMaintenance create(@ApiParam("PreventiveMaintenance") @RequestBody PreventiveMaintenance preventiveMaintenanceReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (preventiveMaintenanceService.canCreate(user, preventiveMaintenanceReq)) {
            return preventiveMaintenanceService.create(preventiveMaintenanceReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasPreventiveMaintenance('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "PreventiveMaintenance not found")})
    public PreventiveMaintenance patch(@ApiParam("PreventiveMaintenance") @RequestBody PreventiveMaintenancePatchDTO preventiveMaintenance, @ApiParam("id") @PathVariable("id") Long id,
                                       HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<PreventiveMaintenance> optionalPreventiveMaintenance = preventiveMaintenanceService.findById(id);

        if (optionalPreventiveMaintenance.isPresent()) {
            PreventiveMaintenance savedPreventiveMaintenance = optionalPreventiveMaintenance.get();
            if (preventiveMaintenanceService.hasAccess(user, savedPreventiveMaintenance) && preventiveMaintenanceService.canPatch(user, preventiveMaintenance)) {
                return preventiveMaintenanceService.update(id, preventiveMaintenance);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("PreventiveMaintenance not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPreventiveMaintenance('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "PreventiveMaintenance not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<PreventiveMaintenance> optionalPreventiveMaintenance = preventiveMaintenanceService.findById(id);
        if (optionalPreventiveMaintenance.isPresent()) {
            PreventiveMaintenance savedPreventiveMaintenance = optionalPreventiveMaintenance.get();
            if (preventiveMaintenanceService.hasAccess(user, savedPreventiveMaintenance)) {
                preventiveMaintenanceService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("PreventiveMaintenance not found", HttpStatus.NOT_FOUND);
    }

}