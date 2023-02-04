package com.grash.controller;

import com.grash.dto.imports.ImportResponse;
import com.grash.dto.imports.WorkOrderImportDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.AssetMapper;
import com.grash.model.OwnUser;
import com.grash.model.WorkOrder;
import com.grash.model.enums.PermissionEntity;
import com.grash.service.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/import")
@Api(tags = "import")
@RequiredArgsConstructor
@Transactional
public class ImportController {

    private final AssetService assetService;
    private final AssetMapper assetMapper;
    private final UserService userService;
    private final LocationService locationService;
    private final PartService partService;
    private final WorkOrderService workOrderService;

    @PostMapping("/work-orders")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ImportResponse importWorkOrders(@Valid @RequestBody List<WorkOrderImportDTO> toImport, HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        final int[] created = {0};
        final int[] updated = {0};
        if (user.getRole().getCreatePermissions().contains(PermissionEntity.WORK_ORDERS)) {
            toImport.forEach(workOrderImportDTO -> {
                Long id = workOrderImportDTO.getId();
                WorkOrder workOrder = new WorkOrder();
                if (id == null) {
                    workOrder.setCompany(user.getCompany());
                    created[0] = created[0] + 1;
                } else {
                    Optional<WorkOrder> optionalWorkOrder = workOrderService.findByIdAndCompany(id, user.getCompany().getId());
                    if (optionalWorkOrder.isPresent()) {
                        workOrder = optionalWorkOrder.get();
                        updated[0] = updated[0] + 1;
                    }
                }
                workOrderService.importWorkOrder(workOrder, workOrderImportDTO, user.getCompany());
            });
            return ImportResponse.builder()
                    .created(created[0])
                    .updated(updated[0])
                    .build();
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }
}
