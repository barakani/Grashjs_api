package com.grash.controller;

import com.grash.dto.CustomerPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.Customer;
import com.grash.model.User;
import com.grash.service.CompanySettingsService;
import com.grash.service.CustomerService;
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
@RequestMapping("/customers")
@Api(tags = "customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;
    private final CompanySettingsService companySettingsService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Customer not found")})
    public Optional<Customer> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer savedCustomer = optionalCustomer.get();
            if (hasAccess(user, savedCustomer)) {
                return optionalCustomer;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Customer create(@ApiParam("Customer") @RequestBody Customer customerReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (canCreate(user, customerReq)) {
            return customerService.create(customerReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Customer not found")})
    public Customer patch(@ApiParam("Customer") @RequestBody CustomerPatchDTO customer, @ApiParam("id") @PathVariable("id") Long id,
                          HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Customer> optionalCustomer = customerService.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer savedCustomer = optionalCustomer.get();
            if (hasAccess(user, savedCustomer)) {
                return customerService.update(id, customer);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Customer not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Customer not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer savedCustomer = optionalCustomer.get();
            if (hasAccess(user, savedCustomer)) {
                customerService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Customer not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Customer customer) {
        return user.getCompany().getCompanySettings().getId().equals(customer.getCompanySettings().getId());
    }

    private boolean canCreate(User user, Customer customerReq) {
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(customerReq.getCompanySettings().getId());
        if (optionalCompanySettings.isPresent()) {
            return user.getCompany().getCompanySettings().getId().equals(optionalCompanySettings.get().getId());
        } else throw new CustomException("Invalid CompanySettings", HttpStatus.NOT_ACCEPTABLE);
    }
}
