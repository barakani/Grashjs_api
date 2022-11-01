package com.grash.controller;

import com.grash.dto.CategoryPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.MeterCategory;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.service.MeterCategoryService;
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
@RequestMapping("/meter-categories")
@Api(tags = "meterCategory")
@RequiredArgsConstructor
public class MeterCategoryController {

    private final MeterCategoryService meterCategoryService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "MeterCategoryCategory not found")})
    public Collection<MeterCategory> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return meterCategoryService.findByCompany(user.getCompany().getId());
        } else return meterCategoryService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "MeterCategory not found")})
    public MeterCategory getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<MeterCategory> optionalMeterCategory = meterCategoryService.findById(id);
        if (optionalMeterCategory.isPresent()) {
            MeterCategory savedMeterCategory = optionalMeterCategory.get();
            if (meterCategoryService.hasAccess(user, savedMeterCategory)) {
                return savedMeterCategory;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public MeterCategory create(@ApiParam("MeterCategory") @Valid @RequestBody MeterCategory meterCategoryReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (meterCategoryService.canCreate(user, meterCategoryReq)) {
            return meterCategoryService.create(meterCategoryReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "MeterCategory not found")})
    public MeterCategory patch(@ApiParam("MeterCategory") @Valid @RequestBody CategoryPatchDTO meterCategory, @ApiParam("id") @PathVariable("id") Long id,
                               HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<MeterCategory> optionalMeterCategory = meterCategoryService.findById(id);

        if (optionalMeterCategory.isPresent()) {
            MeterCategory savedMeterCategory = optionalMeterCategory.get();
            if (meterCategoryService.hasAccess(user, savedMeterCategory) && meterCategoryService.canPatch(user, meterCategory)) {
                return meterCategoryService.update(id, meterCategory);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("MeterCategory not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "MeterCategory not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<MeterCategory> optionalMeterCategory = meterCategoryService.findById(id);
        if (optionalMeterCategory.isPresent()) {
            MeterCategory savedMeterCategory = optionalMeterCategory.get();
            if (meterCategoryService.hasAccess(user, savedMeterCategory)) {
                meterCategoryService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("MeterCategory not found", HttpStatus.NOT_FOUND);
    }

}
