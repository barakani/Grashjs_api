package com.grash.service;

import com.grash.dto.SubscriptionPlanPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.SubscriptionPlanMapper;
import com.grash.model.Company;
import com.grash.model.SubscriptionPlan;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CompanyService companyService;
    private final SubscriptionPlanMapper subscriptionPlanMapper;


    public SubscriptionPlan create(SubscriptionPlan SubscriptionPlan) {
        return subscriptionPlanRepository.save(SubscriptionPlan);
    }

    public SubscriptionPlan update(Long id, SubscriptionPlanPatchDTO subscriptionPlanPatchDTO) {
        if (subscriptionPlanRepository.existsById(id)) {
            SubscriptionPlan savedSubscriptionPlan = subscriptionPlanRepository.findById(id).get();
            return subscriptionPlanRepository.save(subscriptionPlanMapper.updateSubscriptionPlan(savedSubscriptionPlan, subscriptionPlanPatchDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<SubscriptionPlan> getAll() {
        return subscriptionPlanRepository.findAll();
    }

    public void delete(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }

    public Optional<SubscriptionPlan> findById(Long id) {
        return subscriptionPlanRepository.findById(id);
    }

    public Collection<SubscriptionPlan> findByCompany(Long id) {
        return subscriptionPlanRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, SubscriptionPlan subscriptionPlan) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(subscriptionPlan.getCompany().getId());
    }

    public boolean canCreate(User user, SubscriptionPlan subscriptionPlanReq) {
        Long companyId = user.getCompany().getId();
        Optional<Company> optionalCompany = companyService.findById(subscriptionPlanReq.getCompany().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, subscriptionPlanMapper.toDto(subscriptionPlanReq));
    }

    public boolean canPatch(User user, SubscriptionPlanPatchDTO subscriptionPlanReq) {
        return true;
    }
}
