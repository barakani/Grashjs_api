package com.grash.controller;

import com.grash.dto.GeneralPreferencesDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.GeneralPreferences;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.CompanySettingsService;
import com.grash.service.GeneralPreferencesService;
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
@RequestMapping("/general-preferences")
@Api(tags = "generalPreferences")
@RequiredArgsConstructor
public class GeneralPreferencesController {


    private final GeneralPreferencesService generalPreferencesService;
    private final CompanySettingsService companySettingsService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "GeneralPreferences not found")})
    public Collection<GeneralPreferences> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        CompanySettings companySettings = user.getCompany().getCompanySettings();
        return generalPreferencesService.findByCompanySettings(companySettings.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "GeneralPreferences not found")})
    public GeneralPreferences getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<GeneralPreferences> optionalGeneralPreferences = generalPreferencesService.findById(id);
        if (optionalGeneralPreferences.isPresent()) {
            if (hasAccess(user, optionalGeneralPreferences.get())) {
                return generalPreferencesService.findById(id).get();
            } else {
                throw new CustomException("Can't get generalPreferences from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public GeneralPreferences create(@ApiParam("GeneralPreferences") @RequestBody GeneralPreferences generalPreferences, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(generalPreferences.getCompanySettings().getId());
        if (optionalCompanySettings.isPresent()) {
            if (canCreate(user, generalPreferences)) {
                return generalPreferencesService.create(generalPreferences);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Invalid CompanySettings", HttpStatus.NOT_ACCEPTABLE);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public GeneralPreferences patch(@ApiParam("GeneralPreferences") @RequestBody GeneralPreferencesDTO generalPreferencesDTO,
                               @ApiParam("id") @PathVariable("id") Long id,
                               HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<GeneralPreferences> optionalGeneralPreferences = generalPreferencesService.findById(id);

        if (optionalGeneralPreferences.isPresent()) {
            GeneralPreferences generalPreferences = optionalGeneralPreferences.get();
            if (hasAccess(user, generalPreferences)) {
                return generalPreferencesService.update(id, generalPreferencesDTO);
            } else {
                throw new CustomException("Can't patch generalPreferences from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<GeneralPreferences> optionalGeneralPreferences = generalPreferencesService.findById(id);
        if (optionalGeneralPreferences.isPresent()) {
            if (hasAccess(user, optionalGeneralPreferences.get())) {
                generalPreferencesService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("GeneralPreferences not found", HttpStatus.NOT_FOUND);
    }


    //TODO autogenerated categories don't have a createdBy field
    private boolean hasAccess(User user, GeneralPreferences generalPreferences) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return generalPreferences.getCompanySettings().getId()
                .equals(user.getCompany().getCompanySettings().getId());
    }

    private boolean canCreate(User user, GeneralPreferences generalPreferences) {
        Optional<CompanySettings> optionalCompanySettings = companySettingsService
                .findById(generalPreferences.getCompanySettings().getId());
        if (optionalCompanySettings.isPresent()) {
            return user.getCompany().getCompanySettings().getId().equals(optionalCompanySettings.get().getId());
        } else throw new CustomException("Invalid Location", HttpStatus.NOT_ACCEPTABLE);
    }
}
