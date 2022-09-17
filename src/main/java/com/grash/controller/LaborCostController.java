package com.grash.controller;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.LaborCost;
import com.grash.model.User;
import com.grash.service.CompanyService;
import com.grash.service.LaborCostService;
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
@RequestMapping("/labor-costs")
@Api(tags = "laborCost")
@RequiredArgsConstructor
public class LaborCostController {

    private final LaborCostService laborCostService;
    private final CompanyService companyService;
    private final UserService userService;


    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public Collection<LaborCost> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        return laborCostService.findByCompany(user.getCompany().getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public LaborCost getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<LaborCost> optionalLaborCost = laborCostService.findById(id);
        if (optionalLaborCost.isPresent()) {
            if (laborCostService.hasAccess(user, optionalLaborCost.get())) {
                return laborCostService.findById(id).get();
            } else {
                throw new CustomException("Can't get laborCost from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public LaborCost create(@ApiParam("LaborCost") @RequestBody LaborCost laborCost, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Company> optionalCompany = companyService.findById(laborCost.getCompany().getId());
        if (optionalCompany.isPresent()) {
            if (laborCostService.canCreate(user, laborCost)) {
                return laborCostService.create(laborCost);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Invalid Company", HttpStatus.NOT_ACCEPTABLE);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public LaborCost patch(@ApiParam("LaborCost") @RequestBody LaborCostPatchDTO laborCost,
                           @ApiParam("id") @PathVariable("id") Long id,
                           HttpServletRequest req) {
        User user = userService.whoami(req);
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "LaborCost not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<LaborCost> laborCostOptional = laborCostService.findById(id);
        if (laborCostOptional.isPresent()) {
            if (laborCostService.hasAccess(user, laborCostOptional.get())) {
                laborCostService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("LaborCost not found", HttpStatus.NOT_FOUND);
    }


}
