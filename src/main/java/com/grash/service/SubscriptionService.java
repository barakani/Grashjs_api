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
    private final SubscriptionRepository SubscriptionRepository;

    public Subscription create(Subscription Subscription) {
        return SubscriptionRepository.save(Subscription);
    }

    public Subscription update(Subscription Subscription) {
        return SubscriptionRepository.save(Subscription);
    }

    public Collection<Subscription> getAll() { return SubscriptionRepository.findAll(); }

    public void delete(Long id){ SubscriptionRepository.deleteById(id);}

    public Optional<Subscription> findById(Long id) {return SubscriptionRepository.findById(id); }
}
