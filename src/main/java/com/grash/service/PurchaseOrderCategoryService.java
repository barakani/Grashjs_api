package com.grash.service;

import com.grash.dto.PurchaseOrderCategoryPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.PurchaseOrderCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderCategoryService {
    private final PurchaseOrderCategoryRepository purchaseOrderCategoryRepository;
    
    private final CompanySettingsService companySettingsService;


    private final ModelMapper modelMapper;

    public PurchaseOrderCategory create(PurchaseOrderCategory purchaseOrderCategory) {
        return purchaseOrderCategoryRepository.save(purchaseOrderCategory);
    }

    public PurchaseOrderCategory update(Long id, PurchaseOrderCategoryPatchDTO purchaseOrderCategory) {
        if (purchaseOrderCategoryRepository.existsById(id)) {
            PurchaseOrderCategory savedPurchaseOrderCategory = purchaseOrderCategoryRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(purchaseOrderCategory, savedPurchaseOrderCategory);
            return purchaseOrderCategoryRepository.save(savedPurchaseOrderCategory);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<PurchaseOrderCategory> getAll() {
        return purchaseOrderCategoryRepository.findAll();
    }

    public void delete(Long id) {
        purchaseOrderCategoryRepository.deleteById(id);
    }

    public Optional<PurchaseOrderCategory> findById(Long id) {
        return purchaseOrderCategoryRepository.findById(id);
    }

    public Collection<PurchaseOrderCategory> findByCompanySettings(Long id) {
        return purchaseOrderCategoryRepository.findByCompanySettings_Id();
    }

    public boolean hasAccess(User user, PurchaseOrderCategory purchaseOrderCategory) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(purchaseOrderCategory.getCompanySettings().getCompany().getId());
    }

    public boolean canCreate(User user, PurchaseOrderCategory purchaseOrderCategoryReq) {
        Long companyId = user.getCompany().getId();
        
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(purchaseOrderCategoryReq.getCompanySettings().getId());

        boolean first = optionalCompanySettings.isPresent() && optionalCompanySettings.get().getCompany().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(purchaseOrderCategoryReq, PurchaseOrderCategoryPatchDTO.class));
    }

    public boolean canPatch(User user, PurchaseOrderCategoryPatchDTO purchaseOrderCategoryReq) {
        Long companyId = user.getCompany().getId();

        Optional<CompanySettings> optionalCompanySettings = purchaseOrderCategoryReq.getCompanySettings() == null ? Optional.empty() : companySettingsService.findById(purchaseOrderCategoryReq.getCompanySettings().getId());

        boolean first = !optionalCompanySettings.isPresent() || optionalCompanySettings.get().getCompany().getId().equals(companyId);

        return first;
    }

}
