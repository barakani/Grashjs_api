package com.grash.controller;

import com.grash.exception.CustomException;
import com.grash.model.User;
import com.grash.model.UserSettings;
import com.grash.service.UserService;
import com.grash.service.UserSettingsService;
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
@RequestMapping("/userSettings")
@Api(tags = "userSettings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;
    private final UserService userService;


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "UserSettings not found")})
    public Optional<UserSettings> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<UserSettings> optionalUserSettings = userSettingsService.findById(id);
        if (optionalUserSettings.isPresent()) {
            if (optionalUserSettings.get().getId().equals(user.getUserSettings().getId())){
                return userSettingsService.findById(id);
            } else {
                throw new CustomException("Can't get someone else's userSettings", HttpStatus.NOT_ACCEPTABLE);
            }
        } else return null;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "UserSettings not found")})
    public UserSettings patch(@ApiParam("UserSettings") @RequestBody UserSettings userSettings,
                                 @ApiParam("id") @PathVariable("id") Long id,
                                 HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<UserSettings> optionalUserSettings = userSettingsService.findById(id);

        if (optionalUserSettings.isPresent()) {
            UserSettings foundUserSettings = optionalUserSettings.get();
            if (foundUserSettings.getId().equals(user.getUserSettings())) {
                return userSettingsService.update(userSettings);
            } else {
                throw new CustomException("You don't have permission", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new CustomException("Can't get someone else's userSettings", HttpStatus.NOT_ACCEPTABLE);
        }

    }

}
