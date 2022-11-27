package com.grash.controller;

import com.grash.dto.ReadingPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Meter;
import com.grash.model.OwnUser;
import com.grash.model.Reading;
import com.grash.service.MeterService;
import com.grash.service.ReadingService;
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
@RequestMapping("/readings")
@Api(tags = "reading")
@RequiredArgsConstructor
public class ReadingController {

    private final MeterService meterService;
    private final ReadingService readingService;
    private final UserService userService;


    @GetMapping("/meter/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Reading not found")})
    public Collection<Reading> getByMeter(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Meter> optionalMeter = meterService.findById(id);
        if (optionalMeter.isPresent() && meterService.hasAccess(user, optionalMeter.get())) {
            return readingService.findByMeter(id);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Reading create(@ApiParam("Reading") @Valid @RequestBody Reading readingReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (readingService.canCreate(user, readingReq)) {
            return readingService.create(readingReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Reading not found")})
    public Reading patch(@ApiParam("Reading") @Valid @RequestBody ReadingPatchDTO reading, @ApiParam("id") @PathVariable("id") Long id,
                         HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Reading> optionalReading = readingService.findById(id);

        if (optionalReading.isPresent()) {
            Reading savedReading = optionalReading.get();
            if (readingService.hasAccess(user, savedReading) && readingService.canPatch(user, reading)) {
                return readingService.update(id, reading);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Reading not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Reading not found")})
    public ResponseEntity<SuccessResponse> delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<Reading> optionalReading = readingService.findById(id);
        if (optionalReading.isPresent()) {
            Reading savedReading = optionalReading.get();
            if (readingService.hasAccess(user, savedReading)) {
                readingService.delete(id);
                return new ResponseEntity<>(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Reading not found", HttpStatus.NOT_FOUND);
    }
}
