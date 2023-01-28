package com.grash.controller.analytics;

import com.grash.dto.analytics.users.UserWOStats;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.WorkOrder;
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
import java.util.Collection;

@RestController
@RequestMapping("/analytics/users")
@Api(tags = "UserAnalytics")
@RequiredArgsConstructor
public class UserAnalyticsController {

    private final WorkOrderService workOrderService;
    private final UserService userService;

    @GetMapping("/work-orders/overview")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public UserWOStats getWOStats(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<WorkOrder> createdWorkOrders = workOrderService.findByCreatedBy(user.getId());
            Collection<WorkOrder> completedWorkOrders = workOrderService.findByCompletedBy(user.getId());
            return UserWOStats.builder()
                    .created(createdWorkOrders.size())
                    .completed(completedWorkOrders.size())
                    .build();
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

}
