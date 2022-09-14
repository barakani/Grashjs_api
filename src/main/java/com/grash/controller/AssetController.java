package com.grash.controller;

import com.grash.dto.AssetPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Asset;
import com.grash.model.Location;
import com.grash.model.User;
import com.grash.service.AssetService;
import com.grash.service.LocationService;
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
import java.util.Optional;

@RestController
@RequestMapping("/assets")
@Api(tags = "asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;
    private final LocationService locationService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Asset not found")})
    public Optional<Asset> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);
        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (hasAccess(user, savedAsset)) {
                return optionalAsset;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Asset create(@ApiParam("Asset") @RequestBody Asset assetReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (canCreate(user, assetReq)) {
            return assetService.create(assetReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Asset not found")})
    public Asset patch(@ApiParam("Asset") @RequestBody AssetPatchDTO asset, @ApiParam("id") @PathVariable("id") Long id,
                       HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Asset> optionalAsset = assetService.findById(id);

        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (hasAccess(user, savedAsset)) {
                return assetService.update(id, asset);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Asset not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Asset> optionalAsset = assetService.findById(id);
        if (optionalAsset.isPresent()) {
            Asset savedAsset = optionalAsset.get();
            if (hasAccess(user, savedAsset)) {
                assetService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Asset asset) {
        return user.getCompany().getId().equals(
                userService.findById(asset.getCreatedBy()).get().getCompany().getId());
    }

    private boolean canCreate(User user, Asset assetReq) {
        Optional<Location> optionalLocation = locationService.findById(assetReq.getLocation().getId());
        if (optionalLocation.isPresent()) {
            User locationCreator = userService.findById(optionalLocation.get().getCreatedBy()).get();
            return user.getCompany().getId().equals(locationCreator.getCompany().getId());
        } else throw new CustomException("Invalid Location", HttpStatus.NOT_ACCEPTABLE);
    }
}
