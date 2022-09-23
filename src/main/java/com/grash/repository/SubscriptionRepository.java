package com.grash.repository;

import com.grash.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Collection<Subscription> findByCompany_Id(@Param("x") Long id);
}
