package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.exception.CustomException;
import com.grash.mapper.AssetMapper;
import com.grash.model.OwnUser;
import com.grash.model.enums.PermissionEntity;
import com.grash.service.*;
import com.grash.utils.CsvFileGenerator;
import com.grash.utils.Helper;
import com.grash.utils.MultipartFileImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/export")
@Api(tags = "export")
@RequiredArgsConstructor
@Transactional
public class ExportController {

    private final AssetService assetService;
    private final AssetMapper assetMapper;
    private final UserService userService;
    private final LocationService locationService;
    private final PartService partService;
    private final WorkOrderService workOrderService;
    private final CsvFileGenerator csvFileGenerator;
    private final GCPService gcp;

    @GetMapping("/work-orders")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<SuccessResponse> exportWorkOrders(HttpServletRequest req, HttpServletResponse response) throws IOException {
        OwnUser user = userService.whoami(req);

        if (user.getRole().getViewOtherPermissions().contains(PermissionEntity.WORK_ORDERS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(target, StandardCharsets.UTF_8);
            csvFileGenerator.writeWorkOrdersToCsv(workOrderService.findByCompany(user.getCompany().getId()), outputStreamWriter, Helper.getLocale(user));
            byte[] bytes = target.toByteArray();
            MultipartFile file = new MultipartFileImpl(bytes, "Work Orders.csv");
            return ResponseEntity.ok()
                    .body(new SuccessResponse(true, gcp.upload(file, "exports/" + user.getCompany().getId())));
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }
}
