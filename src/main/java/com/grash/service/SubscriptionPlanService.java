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
    private final SubscriptionPlanRepository SubscriptionPlanRepository;

    public SubscriptionPlan create(SubscriptionPlan SubscriptionPlan) {
        return SubscriptionPlanRepository.save(SubscriptionPlan);
    }

    public SubscriptionPlan update(SubscriptionPlan SubscriptionPlan) {
        return SubscriptionPlanRepository.save(SubscriptionPlan);
    }

    public Collection<SubscriptionPlan> getAll() { return SubscriptionPlanRepository.findAll(); }

    public void delete(Long id){ SubscriptionPlanRepository.deleteById(id);}

    public Optional<SubscriptionPlan> findById(Long id) {return SubscriptionPlanRepository.findById(id); }
}
