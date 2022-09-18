package com.grash.service;

import com.grash.dto.SubscriptionPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Subscription;
import com.grash.model.SubscriptionPlan;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private ModelMapper modelMapper;
    private SubscriptionPlanService subscriptionPlanService;
    private CompanyService companyService;

    public Subscription create(Subscription Subscription) {
        return subscriptionRepository.save(Subscription);
    }

    public Subscription update(Long id, SubscriptionPatchDTO relation) {
        if (subscriptionRepository.existsById(id)) {
            Subscription savedSubscription = subscriptionRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(relation, savedSubscription);
            return subscriptionRepository.save(savedSubscription);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    public void delete(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Collection<Subscription> findByCompany(Long id) {
        return subscriptionRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Subscription subscription) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(subscription.getCompany().getId());
    }

    public boolean canCreate(User user, Subscription subscriptionReq) {
        Long companyId = user.getCompany().getId();
        Optional<SubscriptionPlan> optionalSubscriptionPlan = subscriptionPlanService.findById(subscriptionReq.getSubscriptionPlan().getId());
        Optional<Company> optionalCompany = companyService.findById(subscriptionReq.getCompany().getId());

        boolean first = optionalSubscriptionPlan.isPresent() && optionalSubscriptionPlan.get().getCompany().getId().equals(companyId);
        boolean second = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && second && canPatch(user, modelMapper.map(subscriptionReq, SubscriptionPatchDTO.class));
    }

    public boolean canPatch(User user, SubscriptionPatchDTO subscriptionReq) {
        Long companyId = user.getCompany().getId();

        Optional<SubscriptionPlan> optionalSubscriptionPlan = subscriptionReq.getSubscriptionPlan() == null ? Optional.empty() : subscriptionPlanService.findById(subscriptionReq.getSubscriptionPlan().getId());

        boolean first = subscriptionReq.getSubscriptionPlan() == null || (optionalSubscriptionPlan.isPresent() && optionalSubscriptionPlan.get().getCompany().getId().equals(companyId));

        return first;
    }
}
