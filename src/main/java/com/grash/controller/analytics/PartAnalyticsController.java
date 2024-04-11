package com.grash.controller.analytics;

import com.grash.dto.DateRange;
import com.grash.dto.analytics.parts.PartConsumptionsByAsset;
import com.grash.dto.analytics.parts.PartConsumptionsByPart;
import com.grash.dto.analytics.parts.PartConsumptionsByMonth;
import com.grash.dto.analytics.parts.PartStats;
import com.grash.dto.analytics.workOrders.IncompleteWOByAsset;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.Status;
import com.grash.service.AssetService;
import com.grash.service.PartConsumptionService;
import com.grash.service.UserService;
import com.grash.service.WorkOrderService;
import com.grash.utils.Helper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics/parts")
@Api(tags = "PartAnalytics")
@RequiredArgsConstructor
public class PartAnalyticsController {

    private final UserService userService;
    private final AssetService assetService;
    private final WorkOrderService workOrderService;
    private final PartConsumptionService partConsumptionService;

    @PostMapping("/consumptions/overview")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<PartStats> getPartStats(HttpServletRequest req, @RequestBody DateRange dateRange) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<PartConsumption> partConsumptions = partConsumptionService.findByCompanyAndCreatedAtBetween(user.getCompany().getId(), dateRange.getStart(), dateRange.getEnd());
            long totalConsumptionCost = partConsumptions.stream().mapToLong(PartConsumption::getCost).sum();
            int consumedCount = partConsumptions.stream().mapToInt(PartConsumption::getQuantity).sum();

            return ResponseEntity.ok(PartStats.builder()
                    .consumedCount(consumedCount)
                    .totalConsumptionCost(totalConsumptionCost)
                    .build());
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/consumptions/pareto")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<PartConsumptionsByPart>> getPareto(HttpServletRequest req, @RequestBody DateRange dateRange) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<PartConsumption> partConsumptions = partConsumptionService.findByCompanyAndCreatedAtBetween
                            (user.getCompany().getId(), dateRange.getStart(), dateRange.getEnd())
                    .stream().filter(partConsumption -> partConsumption.getQuantity() != 0).collect(Collectors.toList());
            Set<Part> parts = new HashSet<>(partConsumptions.stream()
                    .map(PartConsumption::getPart)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Part::getId)))));
            List<PartConsumptionsByPart> result = parts.stream().map(part -> {
                long cost = partConsumptions.stream().filter(partConsumption -> partConsumption.getPart().getId().equals(part.getId())).mapToLong(PartConsumption::getCost).sum();
                return PartConsumptionsByPart.builder()
                        .id(part.getId())
                        .name(part.getName())
                        .cost(cost).build();
            }).sorted(Comparator.comparing(PartConsumptionsByPart::getCost).reversed()).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

    @PostMapping("/consumptions/assets")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Collection<PartConsumptionsByAsset>> getIncompleteByAsset(HttpServletRequest req, @RequestBody DateRange dateRange) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<Asset> assets = assetService.findByCompanyAndBefore(user.getCompany().getId(), dateRange.getEnd());
            Collection<PartConsumptionsByAsset> result = new ArrayList<>();
            for (Asset asset : assets) {
                Collection<WorkOrder> workOrders = workOrderService.findByAssetAndCreatedAtBetween(asset.getId(), dateRange.getStart(), dateRange.getEnd());
                List<PartConsumption> partConsumptions = partConsumptionService.findByWorkOrders(workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList()));
                long cost = partConsumptions.stream().mapToLong(PartConsumption::getCost).sum();
                result.add(PartConsumptionsByAsset.builder()
                        .cost(cost)
                        .name(asset.getName())
                        .id(asset.getId())
                        .build());
            }
            result = result.stream().filter(partConsumptionsByAsset -> partConsumptionsByAsset.getCost() != 0).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/consumptions/month")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<PartConsumptionsByMonth>> getPartConsumptionsByMonth(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            List<PartConsumptionsByMonth> result = new ArrayList<>();
            LocalDate firstOfMonth =
                    LocalDate.now(ZoneId.of("UTC")).withDayOfMonth(1);
            // .with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            for (int i = 0; i < 13; i++) {
                LocalDate lastOfMonth = firstOfMonth.plusMonths(1).withDayOfMonth(1).minusDays(1);
                Collection<PartConsumption> partConsumptions = partConsumptionService.findByCreatedAtBetweenAndCompany(Helper.localDateToDate(firstOfMonth), Helper.localDateToDate(lastOfMonth), user.getCompany().getId());
                long cost = partConsumptions.stream().mapToLong(PartConsumption::getCost).sum();
                result.add(PartConsumptionsByMonth.builder()
                        .cost(cost)
                        .date(Helper.localDateToDate(firstOfMonth)).build());
                firstOfMonth = firstOfMonth.minusDays(1).withDayOfMonth(1);
            }
            Collections.reverse(result);
            return ResponseEntity.ok(result);
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }
}
