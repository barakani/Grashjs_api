package com.grash.controller;

import com.grash.dto.AssetPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/assets")
@Api(tags = "asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;
    private final CompanyService companyService;
    private final LocationService locationService;
    private final ImageService imageService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final ModelMapper modelMapper;

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
            if (hasAccess(user, savedAsset)) {
                return optionalAsset.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
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
    @PreAuthorize("hasRole('ROLE_CLIENT')")
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
            if (hasAccess(user, savedAsset) && canPatch(user, asset)) {
                return assetService.update(id, asset);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
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
            if (hasAccess(user, savedAsset)) {
                assetService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Asset not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Asset asset) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(asset.getCompany().getId());
    }

    private boolean canCreate(User user, Asset assetReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(assetReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        if (first && canPatch(user, modelMapper.map(assetReq, AssetPatchDTO.class))) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    private boolean canPatch(User user, AssetPatchDTO assetReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = locationService.findById(assetReq.getLocation().getId());
        Optional<Image> optionalImage = assetReq.getImage() == null ? Optional.empty() : imageService.findById(assetReq.getImage().getId());
        Optional<AssetCategory> optionalAssetCategory = assetReq.getCategory() == null ? Optional.empty() : assetCategoryService.findById(assetReq.getCategory().getId());
        Optional<Asset> optionalParentAsset = assetReq.getParentAsset() == null ? Optional.empty() : assetService.findById(assetReq.getParentAsset().getId());
        Optional<User> optionalUser = assetReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(assetReq.getPrimaryUser().getId());
        Optional<Deprecation> optionalDeprecation = assetReq.getDeprecation() == null ? Optional.empty() : deprecationService.findById(assetReq.getDeprecation().getId());

        //@NotNull fields
        boolean second = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);
        //optional fields
        boolean third = !optionalImage.isPresent() || optionalImage.get().getCompany().getId().equals(companyId);
        boolean fourth = !optionalAssetCategory.isPresent() || optionalAssetCategory.get().getCompanySettings().getCompany().getId().equals(companyId);
        boolean fifth = !optionalParentAsset.isPresent() || optionalParentAsset.get().getCompany().getId().equals(companyId);
        boolean sixth = !optionalUser.isPresent() || optionalUser.get().getCompany().getId().equals(companyId);
        boolean seventh = !optionalDeprecation.isPresent() || optionalDeprecation.get().getCompany().getId().equals(companyId);

        if (second && third && fourth && fifth && sixth && seventh) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }
}
