package com.grash.controller;

import com.grash.dto.analytics.WOStats;
import com.grash.dto.analytics.WOStatsByPriority;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.WorkOrder;
import com.grash.model.abstracts.WorkOrderBase;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import com.grash.service.UserService;
import com.grash.service.WorkOrderService;
import com.grash.utils.Helper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/work-order-analytics")
@Api(tags = "WorkOrderAnalytics")
@RequiredArgsConstructor
public class WOAnalyticsController {

    private final WorkOrderService workOrderService;
    private final UserService userService;

    @GetMapping("/overview")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public WOStats getStats(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getViewPermissions().contains(PermissionEntity.ANALYTICS)) {
            Collection<WorkOrder> workOrders = workOrderService.findByCompany(user.getCompany().getId());
            Collection<WorkOrder> completedWO = workOrders.stream().filter(workOrder -> workOrder.getStatus().equals(Status.COMPLETE)).collect(Collectors.toList());
            int total = workOrders.size();
            int complete = completedWO.size();
            int compliant = (int) completedWO.stream().filter(workOrder -> workOrder.getDueDate() == null || workOrder.getCompletedOn().before(workOrder.getDueDate())).count();
            List<Long> completionTimes = completedWO.stream().map(workOrder -> Helper.getDateDiff(Date.from(workOrder.getCreatedAt()), workOrder.getCompletedOn(), TimeUnit.DAYS)).collect(Collectors.toList());
            int averageCycleTime = completionTimes.size() == 0 ? 0 : completionTimes.stream().mapToInt(Long::intValue).sum() / completionTimes.size();
            return WOStats.builder()
                    .total(total)
                    .complete(complete)
                    .compliant(compliant)
                    .avgCycleTime(averageCycleTime).build();
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/incomplete-priority")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public WOStatsByPriority getIncompleteByPriority(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.getRole().getViewPermissions().contains(PermissionEntity.ANALYTICS)) {
            Collection<WorkOrder> workOrders = workOrderService.findByCompany(user.getCompany().getId());
            Collection<WorkOrder> incompleteWO = workOrders.stream().filter(workOrder -> !workOrder.getStatus().equals(Status.COMPLETE)).collect(Collectors.toList());

            List<Integer> highValues = getCountsAndEstimatedDuration(Priority.HIGH, incompleteWO);
            List<Integer> noneValues = getCountsAndEstimatedDuration(Priority.NONE, incompleteWO);
            List<Integer> lowValues = getCountsAndEstimatedDuration(Priority.LOW, incompleteWO);
            List<Integer> mediumValues = getCountsAndEstimatedDuration(Priority.MEDIUM, incompleteWO);

            int highCounts = highValues.get(0);
            int highEstimatedDurations = highValues.get(1);
            int mediumCounts = mediumValues.get(0);
            int mediumEstimatedDurations = mediumValues.get(1);
            int lowCounts = lowValues.get(0);
            int lowEstimatedDurations = lowValues.get(1);
            int noneCounts = noneValues.get(0);
            int noneEstimatedDurations = noneValues.get(1);

            return WOStatsByPriority.builder()
                    .high(WOStatsByPriority.BasicStats.builder()
                            .count(highCounts)
                            .estimatedHours(highEstimatedDurations)
                            .build())
                    .none(WOStatsByPriority.BasicStats.builder()
                            .count(noneCounts)
                            .estimatedHours(noneEstimatedDurations)
                            .build())
                    .low(WOStatsByPriority.BasicStats.builder()
                            .count(lowCounts)
                            .estimatedHours(lowEstimatedDurations)
                            .build())
                    .medium(WOStatsByPriority.BasicStats.builder()
                            .count(mediumCounts)
                            .estimatedHours(mediumEstimatedDurations)
                            .build())
                    .build();

        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

    public List<Integer> getCountsAndEstimatedDuration(Priority priority, Collection<WorkOrder> workOrders) {
        Collection<WorkOrder> priorityWO = workOrders.stream().filter(workOrder -> workOrder.getPriority().equals(priority)).collect(Collectors.toList());
        int priorityCounts = priorityWO.size();
        int priorityEstimatedDurations = priorityWO.stream().map(WorkOrderBase::getEstimatedDuration).mapToInt(value -> value).sum();
        return Arrays.asList(priorityCounts, priorityEstimatedDurations);
    }
}
