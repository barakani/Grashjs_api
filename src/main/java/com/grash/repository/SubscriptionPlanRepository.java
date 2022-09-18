package com.grash.repository;

import com.grash.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface SubscriptionPlanRepository extends JpaRepository<com.grash.model.SubscriptionPlan, Long> {
    @Query("SELECT s from SubscriptionPlan s where s.company.id = :x ")
    Collection<SubscriptionPlan> findByCompany_Id(@Param("x") Long id);
}
