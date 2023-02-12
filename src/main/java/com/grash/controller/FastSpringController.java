package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.fastSpring.Item;
import com.grash.dto.fastSpring.WebhookPayload;
import com.grash.dto.fastSpring.payloads.DeactivatedPayload;
import com.grash.dto.fastSpring.payloads.Order;
import com.grash.dto.fastSpring.payloads.ResumePayload;
import com.grash.dto.fastSpring.payloads.SubscriptionCharge;
import com.grash.exception.CustomException;
import com.grash.model.OwnUser;
import com.grash.model.Subscription;
import com.grash.model.SubscriptionPlan;
import com.grash.service.SubscriptionPlanService;
import com.grash.service.SubscriptionService;
import com.grash.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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

    @Value("${fast-spring.username}")
    private String username;
    @Value("${fast-spring.password}")
    private String password;

    @PostMapping("/new-subscription")
    public void onNewSubscription(@Valid @RequestBody WebhookPayload<Order> order) {
        order.getEvents().forEach(event -> {
            long userId = event.getData().getTags().getUserId();
            Optional<OwnUser> optionalOwnUser = userService.findById(userId);
            if (optionalOwnUser.isPresent()) {
                OwnUser user = optionalOwnUser.get();
                Optional<Subscription> optionalSubscription = subscriptionService.findById(user.getCompany().getSubscription().getId());
                if (optionalSubscription.isPresent()) {
                    Subscription savedSubscription = optionalSubscription.get();
                    Item item = event.getData().getItems().get(0);
                    savedSubscription.setUsersCount(item.getQuantity());
                    setSubscriptionFromFastSpring(item.getSubscription(), savedSubscription);
                    subscriptionService.save(savedSubscription);
                } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
            } else throw new CustomException("User Not Found", HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping("/renew-subscription")
    public void onRenewSubscription(@Valid @RequestBody WebhookPayload<SubscriptionCharge> subscriptionChargeWebhookPayload) {
        subscriptionChargeWebhookPayload.getEvents().forEach(event -> {
            SubscriptionCharge subscriptionCharge = event.getData();
            long userId = subscriptionCharge.getSubscription().getTags().getUserId();
            Optional<OwnUser> optionalOwnUser = userService.findById(userId);
            if (optionalOwnUser.isPresent()) {
                OwnUser user = optionalOwnUser.get();
                Optional<Subscription> optionalSubscription = subscriptionService.findById(user.getCompany().getSubscription().getId());
                if (optionalSubscription.isPresent()) {
                    Subscription savedSubscription = optionalSubscription.get();
                    setSubscriptionFromFastSpring(subscriptionCharge.getSubscription(), savedSubscription);
                    subscriptionService.save(savedSubscription);
                } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
            } else throw new CustomException("User Not Found", HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping("/deactivate")
    public void onDeactivatedSubscription(@Valid @RequestBody WebhookPayload<DeactivatedPayload> deactivatedPayloadWebhookPayload) {
        deactivatedPayloadWebhookPayload.getEvents().forEach(event -> {
                    Optional<Subscription> optionalSubscription = subscriptionService.findByFastSpringId(event.getData().getSubscription());
                    if (optionalSubscription.isPresent()) {
                        Subscription savedSubscription = optionalSubscription.get();
                        subscriptionService.resetToFreePlan(savedSubscription);
                    } else throw new CustomException("Subscription Not found", HttpStatus.NOT_FOUND);
                }
        );
    }

    private void setSubscriptionFromFastSpring(com.grash.dto.fastSpring.Subscription subscription, Subscription savedSubscription) {
        String product = subscription.getProduct();
        boolean monthly = product.contains("monthly");
        savedSubscription.setMonthly(monthly);
        savedSubscription.setActivated(true);
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.findByCode(product.split("-")[0].toUpperCase()).get();
        savedSubscription.setFastSpringId(subscription.getId());
        savedSubscription.setSubscriptionPlan(subscriptionPlan);
        savedSubscription.setStartsOn(new Date(subscription.getBegin()));
        savedSubscription.setEndsOn(new Date(subscription.getNextChargeDate()));
        savedSubscription.setCancelled(false);
    }

    @GetMapping("/cancel")
    public SuccessResponse onCancel(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Subscription> optionalSubscription = subscriptionService.findById(user.getCompany().getSubscription().getId());
        if (optionalSubscription.isPresent()) {
            Subscription savedSubscription = optionalSubscription.get();
            if (!savedSubscription.isActivated()) {
                throw new CustomException("Subscription is not activated", HttpStatus.NOT_ACCEPTABLE);
            }
            if (savedSubscription.isCancelled()) {
                throw new CustomException("Subscription already cancelled", HttpStatus.NOT_ACCEPTABLE);
            }
            cancelRemoteSubscription(savedSubscription.getFastSpringId());
            savedSubscription.setCancelled(true);
            subscriptionService.save(savedSubscription);
            return new SuccessResponse(true, "Subscription cancelled");
        } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/resume")
    public SuccessResponse onResume(HttpServletRequest req) {
        OwnUser user = userService.whoami(req);
        Optional<Subscription> optionalSubscription = subscriptionService.findById(user.getCompany().getSubscription().getId());
        if (optionalSubscription.isPresent()) {
            Subscription savedSubscription = optionalSubscription.get();
            if (!savedSubscription.isActivated()) {
                throw new CustomException("Subscription is not activated", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!savedSubscription.isCancelled()) {
                throw new CustomException("Subscription is active", HttpStatus.NOT_ACCEPTABLE);
            }
            resumeRemoteSubscription(savedSubscription.getFastSpringId());
            savedSubscription.setCancelled(false);
            subscriptionService.save(savedSubscription);
            return new SuccessResponse(true, "Subscription cancelled");
        } else throw new CustomException("Subscription not found", HttpStatus.NOT_FOUND);
    }

    private void cancelRemoteSubscription(String subscriptionId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "https://api.fastspring.com/subscriptions/" + subscriptionId;
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.DELETE,
                request, Object.class);
    }

    private void resumeRemoteSubscription(String subscriptionId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        ResumePayload resumePayload = new ResumePayload(subscriptionId, null);
        HttpEntity<ResumePayload> request = new HttpEntity<>(resumePayload, headers);
        String url = "https://api.fastspring.com/subscriptions/" + subscriptionId;
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST,
                request, Object.class);
    }
}
