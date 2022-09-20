package com.grash.controller;

import com.grash.dto.RelationPatchDTO;
import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.Relation;
import com.grash.model.User;
import com.grash.service.RelationService;
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
@RequestMapping("/relations")
@Api(tags = "relation")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;
    private final UserService userService;


    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Relation not found")})
    public Collection<Relation> getAll(HttpServletRequest req) {
        User user = userService.whoami(req);
        Long companyId = user.getCompany().getId();
        return relationService.findByCompany(companyId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Relation not found")})
    public Relation getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Relation> optionalRelation = relationService.findById(id);
        if (optionalRelation.isPresent()) {
            if (relationService.hasAccess(user, optionalRelation.get())) {
                return relationService.findById(id).get();
            } else {
                throw new CustomException("Can't get relation from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Relation create(@ApiParam("Relation") @Valid @RequestBody Relation relationReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        if (relationService.canCreate(user, relationReq)) {
            return relationService.create(relationReq);
        } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Relation not found")})
    public Relation patch(@ApiParam("Relation") @Valid @RequestBody RelationPatchDTO relation,
                          @ApiParam("id") @PathVariable("id") Long id,
                          HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Relation> optionalRelation = relationService.findById(id);

        if (optionalRelation.isPresent()) {
            Relation savedRelation = optionalRelation.get();
            if (relationService.hasAccess(user, savedRelation) && relationService.canPatch(user, relation)) {
                return relationService.update(id, relation);
            } else {
                throw new CustomException("Can't patch relation from other company", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return null;
        }


    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Relation not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Relation> optionalRelation = relationService.findById(id);
        if (optionalRelation.isPresent()) {
            if (relationService.hasAccess(user, optionalRelation.get())) {
                relationService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Relation not found", HttpStatus.NOT_FOUND);
    }

}