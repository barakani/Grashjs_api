package com.grash.controller;

import com.grash.dto.analytics.assets.TimeCostByAsset;
import com.grash.exception.CustomException;
import com.grash.model.Asset;
import com.grash.model.OwnUser;
import com.grash.model.WorkOrder;
import com.grash.model.enums.Status;
import com.grash.service.AssetService;
import com.grash.service.UserService;
import com.grash.service.WorkOrderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics/assets")
@Api(tags = "AssetAnalytics")
@RequiredArgsConstructor
public class AssetAnalyticsController {

    private final WorkOrderService workOrderService;
    private final UserService userService;
    private final AssetService assetService;

    @GetMapping("/time-cost")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Collection<TimeCostByAsset> getTimeCostByAsset(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<Asset> assets = assetService.findByCompany(user.getCompany().getId());
            Collection<TimeCostByAsset> result = new ArrayList<>();
            assets.forEach(asset -> {
                Collection<WorkOrder> completeWO = workOrderService.findByAsset(asset.getId())
                        .stream().filter(workOrder -> workOrder.getStatus().equals(Status.COMPLETE)).collect(Collectors.toList());
                double time = workOrderService.getLaborCostAndTime(completeWO).getSecond();
                double cost = workOrderService.getAllCost(completeWO);
                result.add(TimeCostByAsset.builder()
                        .time(time)
                        .cost(cost)
                        .name(asset.getName())
                        .id(asset.getId())
                        .build());
            });
            return result;
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }
}
