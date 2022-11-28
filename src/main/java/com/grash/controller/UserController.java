package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.UserInvitationDTO;
import com.grash.mapper.UserMapper;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.service.LocationService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Api(tags = "user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LocationService locationService;
    private final UserMapper userMapper;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "TeamCategory not found")})
    public Collection<OwnUser> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return userService.findByCompany(user.getCompany().getId());
        } else return userService.getAll();
    }

    @PostMapping("/invite")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "TeamCategory not found")})
    public SuccessResponse invite(HttpServletRequest req, @RequestBody UserInvitationDTO invitation) {
        OwnUser user = userService.whoami(req);
        invitation.getEmails().forEach(email -> userService.invite(email, invitation.getRole()));
        return new SuccessResponse(true, "Users have been invited");
    }
}
