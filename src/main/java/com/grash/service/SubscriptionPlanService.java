package com.grash.service;

import com.grash.model.SubscriptionPlan;
import com.grash.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlan create(SubscriptionPlan SubscriptionPlan) {
        return subscriptionPlanRepository.save(SubscriptionPlan);
    }

    public SubscriptionPlan update(SubscriptionPlan SubscriptionPlan) {
        return subscriptionPlanRepository.save(SubscriptionPlan);
    }

    public Collection<SubscriptionPlan> getAll() { return subscriptionPlanRepository.findAll(); }

    public void delete(Long id){ subscriptionPlanRepository.deleteById(id);}

    public Optional<SubscriptionPlan> findById(Long id) {return subscriptionPlanRepository.findById(id); }
}
