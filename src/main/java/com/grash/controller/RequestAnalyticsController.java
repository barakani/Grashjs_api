package com.grash.controller;

import com.grash.dto.analytics.requests.RequestStats;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.Request;
import com.grash.model.WorkOrder;
import com.grash.model.enums.Status;
import com.grash.service.PartConsumptionService;
import com.grash.service.RequestService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics/requests")
@Api(tags = "RequestAnalytics")
@RequiredArgsConstructor
public class RequestAnalyticsController {

    private final UserService userService;
    private final RequestService requestService;
    private final PartConsumptionService partConsumptionService;

    @GetMapping("/overview")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public RequestStats getRequestStats(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        if (user.canSeeAnalytics()) {
            Collection<Request> requests = requestService.findByCompany(user.getCompany().getId());
            Collection<Request> approvedRequests = requests.stream().filter(request -> request.getWorkOrder() != null).collect(Collectors.toList());
            Collection<Request> cancelledRequests = requests.stream().filter(Request::isCancelled).collect(Collectors.toList());
            Collection<Request> pendingRequests = requests.stream().filter(request -> request.getWorkOrder() == null && !request.isCancelled()).collect(Collectors.toList());
            Collection<Request> completeRequests = approvedRequests.stream().filter(request -> request.getWorkOrder().getStatus().equals(Status.COMPLETE)).collect(Collectors.toList());
            long cycleTime = WorkOrder.getAverageAge(completeRequests.stream().map(Request::getWorkOrder).collect(Collectors.toList()));
            return RequestStats.builder()
                    .approved(approvedRequests.size())
                    .pending(pendingRequests.size())
                    .cancelled(cancelledRequests.size())
                    .cycleTime(cycleTime)
                    .build();
        } else throw new CustomException("Access Denied", HttpStatus.FORBIDDEN);
    }

}
