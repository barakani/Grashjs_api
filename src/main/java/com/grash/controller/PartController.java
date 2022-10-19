package com.grash.controller;

import com.grash.dto.PartPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Part;
import com.grash.model.User;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import com.grash.service.PartService;
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
@RequestMapping("/parts")
@Api(tags = "part")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "PartCategory not found")})
    public Collection<Part> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return partService.findByCompany(user.getCompany().getId());
        } else return partService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Part not found")})
    public Part getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Part> optionalPart = partService.findById(id);
        if (optionalPart.isPresent()) {
            Part savedPart = optionalPart.get();
            if (partService.hasAccess(user, savedPart)) {
                return savedPart;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Part create(@ApiParam("Part") @Valid @RequestBody Part partReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (partService.canCreate(user, partReq)) {
            Part savedPart = partService.create(partReq);
            partService.notify(savedPart);
            return savedPart;
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Part not found")})
    public Part patch(@ApiParam("Part") @Valid @RequestBody PartPatchDTO part, @ApiParam("id") @PathVariable("id") Long id,
                      HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Part> optionalPart = partService.findById(id);

        if (optionalPart.isPresent()) {
            Part savedPart = optionalPart.get();
            if (partService.hasAccess(user, savedPart) && partService.canPatch(user, part)) {
                Part patchedPart = partService.update(id, part);
                partService.patchNotify(savedPart, patchedPart);
                return patchedPart;
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Part not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Part not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Part> optionalPart = partService.findById(id);
        if (optionalPart.isPresent()) {
            Part savedPart = optionalPart.get();
            if (partService.hasAccess(user, savedPart)
                    && user.getRole().getPermissions().contains(BasicPermission.DELETE_PARTS_AND_MULTI_PARTS)) {
                partService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Part not found", HttpStatus.NOT_FOUND);
    }

}
