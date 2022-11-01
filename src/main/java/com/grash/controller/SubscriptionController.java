package com.grash.controller;

import com.grash.dto.SubscriptionPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Subscription;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.service.SubscriptionService;
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
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
@Api(tags = "subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "SubscriptionCategory not found")})
    public Collection<Subscription> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return subscriptionService.findByCompany(user.getCompany().getId());
        } else return subscriptionService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Subscription not found")})
    public Subscription getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Subscription> optionalSubscription = subscriptionService.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription savedSubscription = optionalSubscription.get();
            if (subscriptionService.hasAccess(user, savedSubscription)) {
                return savedSubscription;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Subscription create(@ApiParam("Subscription") @Valid @RequestBody Subscription subscriptionReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (subscriptionService.canCreate(user, subscriptionReq)) {
            return subscriptionService.create(subscriptionReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Subscription not found")})
    public Subscription patch(@ApiParam("Subscription") @Valid @RequestBody SubscriptionPatchDTO subscription, @ApiParam("id") @PathVariable("id") Long id,
                              HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Subscription> optionalSubscription = subscriptionService.findById(id);

        if (optionalSubscription.isPresent()) {
            Subscription savedSubscription = optionalSubscription.get();
            if (subscriptionService.hasAccess(user, savedSubscription) && subscriptionService.canPatch(user, subscription)) {
                return subscriptionService.update(id, subscription);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Subscription not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<Subscription> optionalSubscription = subscriptionService.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription savedSubscription = optionalSubscription.get();
            if (subscriptionService.hasAccess(user, savedSubscription)) {
                subscriptionService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
    }

}
