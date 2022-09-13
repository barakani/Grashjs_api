package com.grash.controller;

import com.grash.exception.CustomException;
import com.grash.model.AssetCategory;
import com.grash.model.Company;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.service.AssetCategoryService;
import com.grash.service.CompanyService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/assetCategory")
@Api(tags = "assetCategory")
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;
    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public Optional<AssetCategory> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Company company = user.getCompany();

        Optional<AssetCategory> optionalAssetCategory = assetCategoryService.findById(id);

        if (optionalAssetCategory.isPresent()) {
            if (userService.findById(optionalAssetCategory.get().getCreatedBy()).get().getCompany().getId().equals(company)){
                return assetCategoryService.findById(id);
            } else {
                throw new CustomException("Can't get assetCategory from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return null;
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "AssetCategory not found")})
    public AssetCategory patch(@ApiParam("AssetCategory") @RequestBody AssetCategory assetCategory,
                                 @ApiParam("id") @PathVariable("id") Long id,
                               HttpServletRequest req) {
        User user = userService.whoami(req);
        Company company = user.getCompany();

        Optional<AssetCategory> optionalAssetCategory = assetCategoryService.findById(id);

        if (optionalAssetCategory.isPresent()) {
            AssetCategory savedAssetCategory = optionalAssetCategory.get();
            if (userService.findById(optionalAssetCategory.get().getCreatedBy()).get().getCompany().getId().equals(company)){
                return assetCategoryService.update(assetCategory);
            } else {
                throw new CustomException("Can't patch assetCategory from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return null;
        }


    }

}
