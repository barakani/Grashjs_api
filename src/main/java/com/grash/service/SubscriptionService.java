package com.grash.service;

import com.grash.model.Subscription;
import com.grash.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription create(Subscription Subscription) {
        return subscriptionRepository.save(Subscription);
    }

    public Subscription update(Subscription Subscription) {
        return subscriptionRepository.save(Subscription);
    }

    public Collection<Subscription> getAll() { return subscriptionRepository.findAll(); }

    public void delete(Long id){ subscriptionRepository.deleteById(id);}

    public Optional<Subscription> findById(Long id) {return subscriptionRepository.findById(id); }
}
