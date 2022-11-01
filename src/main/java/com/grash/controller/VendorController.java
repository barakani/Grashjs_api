package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.VendorPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.Vendor;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
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
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
@Api(tags = "vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public Collection<Vendor> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return vendorService.findByCompany(user.getCompany().getId());
        } else return vendorService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Vendor not found")})
    public Vendor getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Vendor> optionalVendor = vendorService.findById(id);
        if (optionalVendor.isPresent()) {
            Vendor savedVendor = optionalVendor.get();
            if (vendorService.hasAccess(user, savedVendor)) {
                return savedVendor;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Vendor create(@ApiParam("Vendor") @Valid @RequestBody Vendor vendorReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (vendorService.canCreate(user, vendorReq)) {
            return vendorService.create(vendorReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Vendor not found")})
    public Vendor patch(@ApiParam("Vendor") @Valid @RequestBody VendorPatchDTO vendor, @ApiParam("id") @PathVariable("id") Long id,
                        HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Vendor> optionalVendor = vendorService.findById(id);

        if (optionalVendor.isPresent()) {
            Vendor savedVendor = optionalVendor.get();
            if (vendorService.hasAccess(user, savedVendor) && vendorService.canPatch(user, vendor)) {
                return vendorService.update(id, vendor);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Vendor not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Vendor not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<Vendor> optionalVendor = vendorService.findById(id);
        if (optionalVendor.isPresent()) {
            Vendor savedVendor = optionalVendor.get();
            if (vendorService.hasAccess(user, savedVendor)
                    && user.getRole().getPermissions().contains(BasicPermission.DELETE_VENDORS_AND_CUSTOMERS)) {
                vendorService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Vendor not found", HttpStatus.NOT_FOUND);
    }

}
