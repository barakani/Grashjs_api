package com.grash.controller;

import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.model.enums.BasicPermission;
import com.grash.service.CompanySettingsService;
import com.grash.service.UserService;
import com.grash.utils.Helper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companySettings")
@Api(tags = "companySettings")
@RequiredArgsConstructor
public class CompanySettingsController {

    private final CompanySettingsService companySettingsService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
    })
    public ResponseEntity<List<CompanySettings>> get(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<CompanySettings> pageActivities = companySettingsService.getAll(paging);
        Helper helper = new Helper();
        HttpHeaders responseHeaders = helper.getPagingHeaders(pageActivities, size, "activities");
        return ResponseEntity.ok()
                .headers(responseHeaders).body(pageActivities.getContent());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Activity not found")})
    public Optional<CompanySettings> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req){

        Optional<CompanySettings> companySettingsOptional = companySettingsService.findById(id);
        User user = userService.whoami(req);

        if (companySettingsOptional.get().equals(user.getCompany())){
            return companySettingsOptional;
        } else {
            throw new CustomException("Can't get someone else's companySettings", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PatchMapping("/{id}")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Application not found")})
    public CompanySettings patch(@ApiParam("CompanySettings") @RequestBody CompanySettings companySettings,
                                 HttpServletRequest req) {

        Optional<CompanySettings> companySettingsOptional = companySettingsService.findById(companySettings.getId());
        User user = userService.whoami(req);

        if (companySettingsOptional.isPresent()){

            CompanySettings foundCompanySettings = companySettingsOptional.get();

            if (foundCompanySettings.equals(user.getCompany())){
                if (user.getRole().getPermissions().contains(BasicPermission.ACCESS_SETTINGS)){
                    return companySettingsService.update(companySettings);
                } else {
                    throw new CustomException("You don't have permission", HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                throw new CustomException("Can't get someone else's application", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new CustomException("CompanySettings not found", HttpStatus.NOT_FOUND);
        }
    }






}
