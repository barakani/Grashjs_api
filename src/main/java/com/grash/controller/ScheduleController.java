package com.grash.controller;

import com.grash.dto.SchedulePatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Schedule;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.ScheduleService;
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
@RequestMapping("/schedules")
@Api(tags = "schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "ScheduleCategory not found")})
    public Collection<Schedule> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return scheduleService.findByCompany(user.getCompany().getId());
        } else return scheduleService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Schedule not found")})
    public Schedule getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Schedule> optionalSchedule = scheduleService.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule savedSchedule = optionalSchedule.get();
            if (scheduleService.hasAccess(user, savedSchedule)) {
                return optionalSchedule.get();
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasSchedule('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Schedule create(@ApiParam("Schedule") @Valid @RequestBody Schedule scheduleReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (scheduleService.canCreate(user, scheduleReq)) {
            return scheduleService.create(scheduleReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasSchedule('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Schedule not found")})
    public Schedule patch(@ApiParam("Schedule") @Valid @RequestBody SchedulePatchDTO schedule, @ApiParam("id") @PathVariable("id") Long id,
                          HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Schedule> optionalSchedule = scheduleService.findById(id);

        if (optionalSchedule.isPresent()) {
            Schedule savedSchedule = optionalSchedule.get();
            if (scheduleService.hasAccess(user, savedSchedule) && scheduleService.canPatch(user, schedule)) {
                return scheduleService.update(id, schedule);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Schedule not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasSchedule('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Schedule not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Schedule> optionalSchedule = scheduleService.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule savedSchedule = optionalSchedule.get();
            if (scheduleService.hasAccess(user, savedSchedule)) {
                scheduleService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Schedule not found", HttpStatus.NOT_FOUND);
    }

}
