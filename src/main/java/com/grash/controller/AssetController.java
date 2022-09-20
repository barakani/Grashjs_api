package com.grash.controller;

import com.grash.dto.AssetPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.dto.manytomany.AddManyToManyDTO;
import com.grash.exception.CustomException;
import com.grash.model.Asset;
import com.grash.model.Notification;
import com.grash.model.User;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.service.AssetService;
import com.grash.service.NotificationService;
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
@RequestMapping("/assets")
@Api(tags = "asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public Collection<Asset> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return assetService.findByCompany(user.getCompany().getId());
        } else return assetService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Asset not found")})
    public Asset getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);
        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (assetService.hasAccess(user, savedAsset)) {
                return savedAsset;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Asset create(@ApiParam("Asset") @Valid @RequestBody Asset assetReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (assetService.canCreate(user, assetReq)) {
            return assetService.create(assetReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Asset not found")})
    public Asset patch(@ApiParam("Asset") @Valid @RequestBody AssetPatchDTO asset, @ApiParam("id") @PathVariable("id") Long id,
                       HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);

        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (assetService.hasAccess(user, savedAsset) && assetService.canPatch(user, asset)) {
                return assetService.update(id, asset);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}/assignedTo")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Asset not found")})
    public Asset addAssignedTo(@ApiParam("Asset") @Valid @RequestBody AddManyToManyDTO assignedToDTO, @ApiParam("id") @PathVariable("id") Long id,
                               HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);
        Optional<User> optionalUser = userService.findById(assignedToDTO.getId());

        if (optionalAsset.isPresent() && optionalUser.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            User assignedTo = optionalUser.get();
            if (assetService.hasAccess(user, savedAsset)
                    && assignedTo.getCompany().getId().equals(user.getCompany().getId())) {
                savedAsset.getAssignedTo().add(assignedTo);
                notificationService.create(new Notification(
                        "Asset " + savedAsset.getName() + " assigned to you",
                        assignedTo, NotificationType.ASSET, savedAsset.getId()));
                return assetService.save(savedAsset);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Asset not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Asset> optionalAsset = assetService.findById(id);
        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (assetService.hasAccess(user, savedAsset)
                    && user.getRole().getPermissions().contains(BasicPermission.DELETE_ASSETS)) {
                assetService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
    }

}
