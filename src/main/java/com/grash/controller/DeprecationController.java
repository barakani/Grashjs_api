package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Deprecation;
import com.grash.model.User;
import com.grash.service.DeprecationService;
import com.grash.service.LocationService;
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
import java.util.Optional;

@RestController
@RequestMapping("/deprecations")
@Api(tags = "deprecation")
@RequiredArgsConstructor
public class DeprecationController {

    private final DeprecationService deprecationService;
    private final UserService userService;
    private final LocationService locationService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Deprecation not found")})
    public Optional<Deprecation> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Deprecation> optionalDeprecation = deprecationService.findById(id);
        if (optionalDeprecation.isPresent()) {
            Deprecation savedDeprecation = optionalDeprecation.get();
            if (hasAccess(user, savedDeprecation)) {
                return optionalDeprecation;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Deprecation create(@ApiParam("Deprecation") @RequestBody Deprecation deprecationReq) {
        return deprecationService.create(deprecationReq);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Deprecation not found")})
    public Deprecation patch(@ApiParam("Deprecation") @RequestBody Deprecation deprecation, @ApiParam("id") @PathVariable("id") Long id,
                             HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Deprecation> optionalDeprecation = deprecationService.findById(id);

        if (optionalDeprecation.isPresent()) {
            Deprecation savedDeprecation = optionalDeprecation.get();
            if (hasAccess(user, savedDeprecation)) {
                return deprecationService.update(id, deprecation);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Deprecation not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Deprecation not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Deprecation> optionalDeprecation = deprecationService.findById(id);
        if (optionalDeprecation.isPresent()) {
            Deprecation savedDeprecation = optionalDeprecation.get();
            if (hasAccess(user, savedDeprecation)) {
                deprecationService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Deprecation not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Deprecation deprecation) {
        return user.getCompany().getId().equals(
                userService.findById(deprecation.getCreatedBy()).get().getCompany().getId());
    }
}
