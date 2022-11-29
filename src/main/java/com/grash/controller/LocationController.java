package com.grash.controller;

import com.grash.dto.LocationPatchDTO;
import com.grash.dto.LocationShowDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.mapper.LocationMapper;
import com.grash.model.Location;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.service.LocationService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
@Api(tags = "location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "LocationCategory not found")})
    public List<LocationShowDTO> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return locationService.findByCompany(user.getCompany().getId()).stream().map(locationMapper::toShowDto).collect(Collectors.toList());
        } else
            return locationService.getAll().stream().map(locationMapper::toShowDto).collect(Collectors.toList());
    }

    @GetMapping("/children/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Location not found")})
    public Collection<LocationShowDTO> getChildrenById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (id.equals(0L) && user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return locationService.findByCompany(user.getCompany().getId()).stream().filter(location -> location.getParentLocation() == null).map(locationMapper::toShowDto).collect(Collectors.toList());
        }
        Optional<Location> optionalLocation = locationService.findById(id);
        if (optionalLocation.isPresent()) {
            Location savedLocation = optionalLocation.get();
            if (locationService.hasAccess(user, savedLocation)) {
                return locationService.findLocationChildren(id).stream().map(locationMapper::toShowDto).collect(Collectors.toList());
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);

        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Location not found")})
    public LocationShowDTO getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Location> optionalLocation = locationService.findById(id);
        if (optionalLocation.isPresent()) {
            Location savedLocation = optionalLocation.get();
            if (locationService.hasAccess(user, savedLocation)) {
                return locationMapper.toShowDto(savedLocation);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public LocationShowDTO create(@ApiParam("Location") @Valid @RequestBody Location locationReq, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (locationService.canCreate(user, locationReq)) {
            if (locationReq.getParentLocation() != null) {
                Optional<Location> optionalParentLocation = locationService.findById(locationReq.getParentLocation().getId());
                if (optionalParentLocation.isPresent()) {
                    Location parentLocation = optionalParentLocation.get();
                    if (parentLocation.getParentLocation() != null) {
                        throw new CustomException("Parent location has a Parent Location ", HttpStatus.NOT_ACCEPTABLE);
                    }
                    parentLocation.setHasChildren(true);
                    locationService.save(parentLocation);
                } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
            }
            Location savedLocation = locationService.create(locationReq);
            locationService.notify(savedLocation);
            return locationMapper.toShowDto(savedLocation);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Location not found")})
    public LocationShowDTO patch(@ApiParam("Location") @Valid @RequestBody LocationPatchDTO location, @ApiParam("id") @PathVariable("id") Long id,
                                 HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Location> optionalLocation = locationService.findById(id);
        if (optionalLocation.isPresent()) {
            Location savedLocation = optionalLocation.get();
            if (locationService.hasAccess(user, savedLocation) && locationService.canPatch(user, location)) {
                Location patchedLocation = locationService.update(id, location);
                locationService.patchNotify(savedLocation, patchedLocation);
                return locationMapper.toShowDto(patchedLocation);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Location not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Location not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<Location> optionalLocation = locationService.findById(id);
        if (optionalLocation.isPresent()) {
            Location savedLocation = optionalLocation.get();
            if (locationService.hasAccess(user, savedLocation)) {
                locationService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Location not found", HttpStatus.NOT_FOUND);
    }

}
