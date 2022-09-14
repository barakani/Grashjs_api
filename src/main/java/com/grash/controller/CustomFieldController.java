package com.grash.controller;

import com.grash.dto.CustomFieldPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.CustomField;
import com.grash.model.User;
import com.grash.model.Vendor;
import com.grash.service.CustomFieldService;
import com.grash.service.UserService;
import com.grash.service.VendorService;
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
@RequestMapping("/customFields")
@Api(tags = "customField")
@RequiredArgsConstructor
public class CustomFieldController {

    private final CustomFieldService customFieldService;
    private final UserService userService;
    private final VendorService vendorService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "CustomField not found")})
    public Optional<CustomField> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<CustomField> optionalCustomField = customFieldService.findById(id);
        if (optionalCustomField.isPresent()) {
            CustomField savedCustomField = optionalCustomField.get();
            if (hasAccess(user, savedCustomField)) {
                return optionalCustomField;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public CustomField create(@ApiParam("CustomField") @RequestBody CustomField customFieldReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (canCreate(user, customFieldReq)) {
            return customFieldService.create(customFieldReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "CustomField not found")})
    public CustomField patch(@ApiParam("CustomField") @RequestBody CustomFieldPatchDTO customField, @ApiParam("id") @PathVariable("id") Long id,
                             HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<CustomField> optionalCustomField = customFieldService.findById(id);

        if (optionalCustomField.isPresent()) {
            CustomField savedCustomField = optionalCustomField.get();
            if (hasAccess(user, savedCustomField)) {
                return customFieldService.update(id, customField);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("CustomField not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "CustomField not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<CustomField> optionalCustomField = customFieldService.findById(id);
        if (optionalCustomField.isPresent()) {
            CustomField savedCustomField = optionalCustomField.get();
            if (hasAccess(user, savedCustomField)) {
                customFieldService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("CustomField not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, CustomField customField) {
        return user.getCompany().getId().equals(customField.getVendor().getCompany().getId());
    }

    private boolean canCreate(User user, CustomField customFieldReq) {
        Optional<Vendor> optionalVendor = vendorService.findById(customFieldReq.getVendor().getId());
        if (optionalVendor.isPresent()) {
            return user.getCompany().getId().equals(optionalVendor.get().getCompany().getId());
        } else throw new CustomException("Invalid Vendor", HttpStatus.NOT_ACCEPTABLE);
    }
}
