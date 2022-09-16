package com.grash.controller;

import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.GeneralPreferences;
import com.grash.model.User;
import com.grash.service.GeneralPreferencesService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/general-preferences")
@Api(tags = "generalPreferences")
@RequiredArgsConstructor
public class GeneralPreferencesController {


    private final GeneralPreferencesService generalPreferencesService;
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
            if (generalPreferencesService.hasAccess(user, optionalGeneralPreferences.get())) {
                return generalPreferencesService.findById(id).get();
            } else {
                throw new CustomException("Can't get generalPreferences from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }


}
