package com.grash.controller;

import com.grash.dto.CategoryPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.TimeCategory;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.TimeCategoryService;
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
@RequestMapping("/timeCategorys")
@Api(tags = "timeCategory")
@RequiredArgsConstructor
public class TimeCategoryController {

    private final TimeCategoryService timeCategoryService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "TimeCategoryCategory not found")})
    public Collection<TimeCategory> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return timeCategoryService.findByCompanySettings(user.getCompany().getCompanySettings().getId());
        } else return timeCategoryService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "TimeCategory not found")})
    public TimeCategory getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<TimeCategory> optionalTimeCategory = timeCategoryService.findById(id);
        if (optionalTimeCategory.isPresent()) {
            TimeCategory savedTimeCategory = optionalTimeCategory.get();
            if (timeCategoryService.hasAccess(user, savedTimeCategory)) {
                return optionalTimeCategory.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public TimeCategory create(@ApiParam("TimeCategory") @RequestBody TimeCategory timeCategoryReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (timeCategoryService.canCreate(user, timeCategoryReq)) {
            return timeCategoryService.create(timeCategoryReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "TimeCategory not found")})
    public TimeCategory patch(@ApiParam("TimeCategory") @RequestBody CategoryPatchDTO timeCategory, @ApiParam("id") @PathVariable("id") Long id,
                              HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<TimeCategory> optionalTimeCategory = timeCategoryService.findById(id);

        if (optionalTimeCategory.isPresent()) {
            TimeCategory savedTimeCategory = optionalTimeCategory.get();
            if (timeCategoryService.hasAccess(user, savedTimeCategory) && timeCategoryService.canPatch(user, timeCategory)) {
                return timeCategoryService.update(id, timeCategory);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("TimeCategory not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "TimeCategory not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<TimeCategory> optionalTimeCategory = timeCategoryService.findById(id);
        if (optionalTimeCategory.isPresent()) {
            TimeCategory savedTimeCategory = optionalTimeCategory.get();
            if (timeCategoryService.hasAccess(user, savedTimeCategory)) {
                timeCategoryService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("TimeCategory not found", HttpStatus.NOT_FOUND);
    }

}
