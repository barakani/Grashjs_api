package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.TeamPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Team;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.service.TeamService;
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
@RequestMapping("/teams")
@Api(tags = "team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "TeamCategory not found")})
    public Collection<Team> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return teamService.findByCompany(user.getCompany().getId());
        } else return teamService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Team not found")})
    public Team getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Team> optionalTeam = teamService.findById(id);
        if (optionalTeam.isPresent()) {
            Team savedTeam = optionalTeam.get();
            if (teamService.hasAccess(user, savedTeam)) {
                return savedTeam;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Team create(@ApiParam("Team") @Valid @RequestBody Team teamReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (teamService.canCreate(user, teamReq)) {
            return teamService.create(teamReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Team not found")})
    public Team patch(@ApiParam("Team") @Valid @RequestBody TeamPatchDTO team, @ApiParam("id") @PathVariable("id") Long id,
                      HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Team> optionalTeam = teamService.findById(id);

        if (optionalTeam.isPresent()) {
            Team savedTeam = optionalTeam.get();
            if (teamService.hasAccess(user, savedTeam) && teamService.canPatch(user, team)) {
                return teamService.update(id, team);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Team not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Team not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Team> optionalTeam = teamService.findById(id);
        if (optionalTeam.isPresent()) {
            Team savedTeam = optionalTeam.get();
            if (teamService.hasAccess(user, savedTeam)) {
                teamService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Team not found", HttpStatus.NOT_FOUND);
    }

}
