package com.grash.controller;

import com.grash.dto.SubscriptionPlanPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.SubscriptionPlan;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.SubscriptionPlanService;
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
@RequestMapping("/subscriptionPlans")
@Api(tags = "subscriptionPlan")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "SubscriptionPlanCategory not found")})
    public Collection<SubscriptionPlan> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return subscriptionPlanService.findByCompany(user.getCompany().getId());
        } else return subscriptionPlanService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "SubscriptionPlan not found")})
    public SubscriptionPlan getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<SubscriptionPlan> optionalSubscriptionPlan = subscriptionPlanService.findById(id);
        if (optionalSubscriptionPlan.isPresent()) {
            SubscriptionPlan savedSubscriptionPlan = optionalSubscriptionPlan.get();
            if (subscriptionPlanService.hasAccess(user, savedSubscriptionPlan)) {
                return optionalSubscriptionPlan.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasSubscriptionPlan('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public SubscriptionPlan create(@ApiParam("SubscriptionPlan") @RequestBody SubscriptionPlan subscriptionPlanReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (subscriptionPlanService.canCreate(user, subscriptionPlanReq)) {
            return subscriptionPlanService.create(subscriptionPlanReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasSubscriptionPlan('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "SubscriptionPlan not found")})
    public SubscriptionPlan patch(@ApiParam("SubscriptionPlan") @RequestBody SubscriptionPlanPatchDTO subscriptionPlan, @ApiParam("id") @PathVariable("id") Long id,
                                  HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<SubscriptionPlan> optionalSubscriptionPlan = subscriptionPlanService.findById(id);

        if (optionalSubscriptionPlan.isPresent()) {
            SubscriptionPlan savedSubscriptionPlan = optionalSubscriptionPlan.get();
            if (subscriptionPlanService.hasAccess(user, savedSubscriptionPlan) && subscriptionPlanService.canPatch(user, subscriptionPlan)) {
                return subscriptionPlanService.update(id, subscriptionPlan);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("SubscriptionPlan not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasSubscriptionPlan('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "SubscriptionPlan not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<SubscriptionPlan> optionalSubscriptionPlan = subscriptionPlanService.findById(id);
        if (optionalSubscriptionPlan.isPresent()) {
            SubscriptionPlan savedSubscriptionPlan = optionalSubscriptionPlan.get();
            if (subscriptionPlanService.hasAccess(user, savedSubscriptionPlan)) {
                subscriptionPlanService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("SubscriptionPlan not found", HttpStatus.NOT_FOUND);
    }

}