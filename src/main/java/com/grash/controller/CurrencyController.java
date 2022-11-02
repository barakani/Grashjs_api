package com.grash.controller;

import com.grash.dto.CurrencyPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Currency;
import com.grash.model.User;
import com.grash.model.enums.BasicPermission;
import com.grash.service.CurrencyService;
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
@RequestMapping("/currencies")
@Api(tags = "currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    private final UserService userService;

    private final static String CURRENCY_NOT_FOUND = "Currency not found";

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = CURRENCY_NOT_FOUND)})
    public Collection<Currency> getAll(HttpServletRequest req) {
        return currencyService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = CURRENCY_NOT_FOUND)})
    public Currency getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Currency> optionalCurrency = currencyService.findById(id);
        if (optionalCurrency.isPresent()) {
            Currency savedCurrency = optionalCurrency.get();
            if (currencyService.hasAccess(user)) {
                return savedCurrency;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Currency create(@ApiParam("Asset") @Valid @RequestBody Currency currency, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (currencyService.canCreate(user)) {
            return currencyService.create(currency);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = CURRENCY_NOT_FOUND)})
    public Currency patch(@ApiParam("Asset") @Valid @RequestBody CurrencyPatchDTO currencyPatchDTO, @ApiParam("id") @PathVariable("id") Long id,
                          HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Currency> optionalCurrency = currencyService.findById(id);

        if (optionalCurrency.isPresent()) {
            Currency currency = optionalCurrency.get();
            if (currencyService.hasAccess(user) && currencyService.canPatch(user)) {
                return currencyService.update(id, currencyPatchDTO);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Currency not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = CURRENCY_NOT_FOUND)})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Currency> optionalCurrency = currencyService.findById(id);
        if (optionalCurrency.isPresent()) {
            if (currencyService.hasAccess(user)
                    && user.getRole().getPermissions().contains(BasicPermission.DELETE_CURRENCIES)) {
                currencyService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException(CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
