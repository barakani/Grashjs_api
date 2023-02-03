package com.grash.controller;

import com.grash.exception.CustomException;
import com.grash.mapper.AssetMapper;
import com.grash.model.OwnUser;
import com.grash.model.enums.PermissionEntity;
import com.grash.service.*;
import com.grash.utils.CsvFileGenerator;
import com.grash.utils.Helper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @GetMapping("/work-orders")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void exportWorkOrders(HttpServletRequest req, HttpServletResponse response) throws IOException {
        OwnUser user = userService.whoami(req);

        if (user.getRole().getViewOtherPermissions().contains(PermissionEntity.WORK_ORDERS)) {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=\"work-orders.csv\"");
            csvFileGenerator.writeWorkOrdersToCsv(workOrderService.findByCompany(user.getCompany().getId()), response.getWriter(), Helper.getLocale(user));

        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }
}
