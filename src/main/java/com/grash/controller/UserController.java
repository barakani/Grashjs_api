package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.UserInvitationDTO;
import com.grash.dto.UserPatchDTO;
import com.grash.dto.UserResponseDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.UserMapper;
import com.grash.model.OwnUser;
import com.grash.model.Role;
import com.grash.model.enums.RoleType;
import com.grash.service.RoleService;
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

@RestController
@RequestMapping("/users")
@Api(tags = "user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
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
        Optional<Role> optionalRole = roleService.findById(invitation.getRole().getId());
        if (optionalRole.isPresent() && user.getCompany().getCompanySettings().getId().equals(optionalRole.get().getCompanySettings().getId())) {
            invitation.getEmails().forEach(email -> userService.invite(email, optionalRole.get()));
            return new SuccessResponse(true, "Users have been invited");
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);

    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "User not found")})
    public UserResponseDTO patch(@ApiParam("User") @Valid @RequestBody UserPatchDTO userReq,
                                 @ApiParam("id") @PathVariable("id") Long id,
                                 HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<OwnUser> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            OwnUser savedOwnUser = optionalUser.get();
            if (savedOwnUser.getId().equals(user.getId())) {
                return userMapper.toDto(userService.update(id, userReq));
            } else {
                throw new CustomException("You don't have permission", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new CustomException("Can't get someone else's user", HttpStatus.NOT_ACCEPTABLE);
        }

    }
}
