package com.grash.controller;


import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.model.File;
import com.grash.model.OwnUser;
import com.grash.model.enums.FileType;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.RoleType;
import com.grash.service.FileService;
import com.grash.service.GCPService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


@RestController
@Api(tags = "file")
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final GCPService gcp;
    private final FileService fileService;
    private final UserService userService;

    @PostMapping(value = "/upload", produces = "application/json")
    public Collection<File> handleFileUpload(@RequestParam("files") MultipartFile[] filesReq, @RequestParam("folder") String folder, HttpServletRequest req, @RequestParam("type") FileType fileType) {
        OwnUser user = userService.whoami(req);
        Collection<File> result = new ArrayList<>();
        Arrays.asList(filesReq).forEach(fileReq -> {
            String url = gcp.upload(fileReq, folder);
            result.add(fileService.create(new File(fileReq.getOriginalFilename(), url, user.getCompany(), fileType)));
        });
        return result;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "FileCategory not found")})
    public Collection<File> getAll(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT)) {
            return fileService.findByCompany(user.getCompany().getId());
        } else return fileService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "File not found")})
    public File getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<File> optionalFile = fileService.findById(id);
        if (optionalFile.isPresent()) {
            File savedFile = optionalFile.get();
            if (fileService.hasAccess(user, savedFile)) {
                return savedFile;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "File not found")})
    public File patch(@ApiParam("File") @Valid @RequestBody File file, @ApiParam("id") @PathVariable("id") Long id,
                      HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<File> optionalFile = fileService.findById(id);

        if (optionalFile.isPresent()) {
            File savedFile = optionalFile.get();
            if (fileService.hasAccess(user, savedFile)) {
                return fileService.update(file);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("File not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "File not found")})
    public ResponseEntity<SuccessResponse> delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);

        Optional<File> optionalFile = fileService.findById(id);
        if (optionalFile.isPresent()) {
            File savedFile = optionalFile.get();
            if (fileService.hasAccess(user, savedFile)
                    && (user.getId().equals(savedFile.getCreatedBy())
                    || user.getRole().getDeleteOtherPermissions().contains(PermissionEntity.FILES))) {
                fileService.delete(id);
                return new ResponseEntity<>(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("File not found", HttpStatus.NOT_FOUND);
    }

}
