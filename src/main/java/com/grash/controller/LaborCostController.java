package com.grash.controller;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.LaborCost;
import com.grash.model.OwnUser;
import com.grash.service.LaborCostService;
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
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/labor-costs")
@Api(tags = "laborCost")
@RequiredArgsConstructor
public class LaborCostController {

    private final LaborCostService laborCostService;
    private final UserService userService;


    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public Collection<LaborCost> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        return laborCostService.getAll().stream().filter(laborCost ->
                laborCost.getLabor().getCompany().getId().equals(user.getCompany().getId())).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public LaborCost getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<LaborCost> optionalLaborCost = laborCostService.findById(id);
        if (optionalLaborCost.isPresent()) {
            if (laborCostService.hasAccess(user, optionalLaborCost.get())) {
                return laborCostService.findById(id).get();
            } else {
                throw new CustomException("Can't get laborCost from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public LaborCost patch(@ApiParam("LaborCost") @Valid @RequestBody LaborCostPatchDTO laborCost,
                           @ApiParam("id") @PathVariable("id") Long id,
                           HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<LaborCost> optionalLaborCost = laborCostService.findById(id);

        if (optionalLaborCost.isPresent()) {
            LaborCost savedLaborCost = optionalLaborCost.get();
            if (laborCostService.hasAccess(user, savedLaborCost) && laborCostService.canPatch(user, laborCost)) {
                return laborCostService.update(id, laborCost);
            } else {
                throw new CustomException("Can't patch laborCost from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return null;
        }


    }


}
