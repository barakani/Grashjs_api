package com.grash.controller;

import com.grash.dto.ChecklistPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Checklist;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.ChecklistService;
import com.grash.service.CompanySettingsService;
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
@RequestMapping("/checklists")
@Api(tags = "checklist")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;
    private final UserService userService;
    private final CompanySettingsService companySettingsService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public Collection<Checklist> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            CompanySettings companySettings = user.getCompany().getCompanySettings();
            return checklistService.findByCompanySettings(companySettings.getId());
        } else return checklistService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Checklist not found")})
    public Optional<Checklist> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Checklist> optionalChecklist = checklistService.findById(id);
        if (optionalChecklist.isPresent()) {
            Checklist savedChecklist = optionalChecklist.get();
            if (hasAccess(user, savedChecklist)) {
                return optionalChecklist;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Checklist create(@ApiParam("Checklist") @RequestBody Checklist checklistReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (canCreate(user, checklistReq)) {
            return checklistService.create(checklistReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Checklist not found")})
    public Checklist patch(@ApiParam("Checklist") @RequestBody ChecklistPatchDTO checklist, @ApiParam("id") @PathVariable("id") Long id,
                           HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Checklist> optionalChecklist = checklistService.findById(id);

        if (optionalChecklist.isPresent()) {
            Checklist savedChecklist = optionalChecklist.get();
            if (hasAccess(user, savedChecklist)) {
                return checklistService.update(id, checklist);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Checklist not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Checklist not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Checklist> optionalChecklist = checklistService.findById(id);
        if (optionalChecklist.isPresent()) {
            Checklist savedChecklist = optionalChecklist.get();
            if (hasAccess(user, savedChecklist)) {
                checklistService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Checklist not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Checklist checklist) {
        return user.getCompany().getCompanySettings().getId().equals(checklist.getCompanySettings().getId());
    }

    private boolean canCreate(User user, Checklist checklistReq) {
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(checklistReq.getCompanySettings().getId());
        if (optionalCompanySettings.isPresent()) {
            return user.getCompany().getCompanySettings().getId().equals(optionalCompanySettings.get().getId());
        } else throw new CustomException("Invalid CompanySettings", HttpStatus.NOT_ACCEPTABLE);
    }
}
