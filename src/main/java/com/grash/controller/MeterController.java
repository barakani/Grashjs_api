package com.grash.controller;

import com.grash.dto.MeterPatchDTO;
import com.grash.dto.MeterShowDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.mapper.MeterMapper;
import com.grash.model.Meter;
import com.grash.model.OwnUser;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.RoleType;
import com.grash.service.MeterService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meters")
@Api(tags = "meter")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;
    private final MeterMapper meterMapper;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "MeterCategory not found")})
    public Collection<MeterShowDTO> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return meterService.findByCompany(user.getCompany().getId()).stream().map(meterMapper::toShowDto).collect(Collectors.toList());
        } else return meterService.getAll().stream().map(meterMapper::toShowDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Meter not found")})
    public MeterShowDTO getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Meter> optionalMeter = meterService.findById(id);
        if (optionalMeter.isPresent()) {
            Meter savedMeter = optionalMeter.get();
            if (meterService.hasAccess(user, savedMeter)) {
                return meterMapper.toShowDto(savedMeter);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public MeterShowDTO create(@ApiParam("Meter") @Valid @RequestBody Meter meterReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (meterService.canCreate(user, meterReq)) {
            Meter savedMeter = meterService.create(meterReq);
            meterService.notify(savedMeter);
            return meterMapper.toShowDto(savedMeter);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Meter not found")})
    public MeterShowDTO patch(@ApiParam("Meter") @Valid @RequestBody MeterPatchDTO meter, @ApiParam("id") @PathVariable("id") Long id,
                              HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Meter> optionalMeter = meterService.findById(id);

        if (optionalMeter.isPresent()) {
            Meter savedMeter = optionalMeter.get();
            if (meterService.hasAccess(user, savedMeter) && meterService.canPatch(user, meter)) {
                Meter patchedMeter = meterService.update(id, meter);
                meterService.patchNotify(savedMeter, patchedMeter);
                return meterMapper.toShowDto(patchedMeter);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Meter not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Meter not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<Meter> optionalMeter = meterService.findById(id);
        if (optionalMeter.isPresent()) {
            Meter savedMeter = optionalMeter.get();
            if (meterService.hasAccess(user, savedMeter)
                    && user.getRole().getDeleteOtherPermissions().contains(PermissionEntity.METERS)) {
                meterService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Meter not found", HttpStatus.NOT_FOUND);
    }

}
