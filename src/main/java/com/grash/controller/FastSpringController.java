package com.grash.controller;

import com.grash.dto.fastSpring.OrderDTO;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.Subscription;
import com.grash.model.SubscriptionPlan;
import com.grash.service.SubscriptionPlanService;
import com.grash.service.SubscriptionService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/fast-spring")
@Api(tags = "fastSpring")
@RequiredArgsConstructor
@Transactional
public class FastSpringController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/order-completed")
    public void onSubscriptionChange(@Valid @RequestBody OrderDTO order) {
        long userId = order.getEvents().get(0).getData().getTags().getUserId();
        Optional<OwnUser> optionalOwnUser = userService.findById(userId);
        if (optionalOwnUser.isPresent()) {
            OwnUser user = optionalOwnUser.get();
            Optional<Subscription> optionalSubscription = subscriptionService.findById(user.getCompany().getSubscription().getId());
            if (optionalSubscription.isPresent()) {
                Subscription savedSubscription = optionalSubscription.get();
                OrderDTO.Item item = order.getEvents().get(0).getData().getItems().get(0);
                savedSubscription.setUsersCount(item.getQuantity());
                String product = item.getProduct();
                boolean monthly = product.contains("monthly");
                savedSubscription.setMonthly(monthly);
                SubscriptionPlan subscriptionPlan = subscriptionPlanService.findByCode(product.split("-")[0].toUpperCase()).get();
                savedSubscription.setSubscriptionPlan(subscriptionPlan);
                savedSubscription.setStartsOn(new Date(item.getSubscription().getBegin()));
                savedSubscription.setEndsOn(new Date(item.getSubscription().getNextChargeDate()));
                subscriptionService.save(savedSubscription);
            } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
        } else throw new CustomException("User Not Found", HttpStatus.NOT_FOUND);
    }
}
