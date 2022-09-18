package com.grash.service;

import com.grash.dto.PurchaseOrderPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.PurchaseOrder;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final CompanyService companyService;
    private final VendorService vendorService;


    private final ModelMapper modelMapper;

    public PurchaseOrder create(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder update(Long id, PurchaseOrderPatchDTO purchaseOrder) {
        if (purchaseOrderRepository.existsById(id)) {
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(purchaseOrder, savedPurchaseOrder);
            return purchaseOrderRepository.save(savedPurchaseOrder);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public void delete(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    public Optional<PurchaseOrder> findById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public Collection<PurchaseOrder> findByCompany(Long id) {
        return purchaseOrderRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, PurchaseOrder purchaseOrder) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(purchaseOrder.getCompany().getId());
    }

    public boolean canCreate(User user, PurchaseOrder purchaseOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(purchaseOrderReq.getCompany().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(purchaseOrderReq, PurchaseOrderPatchDTO.class));
    }

    public boolean canPatch(User user, PurchaseOrderPatchDTO purchaseOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = purchaseOrderReq.getCompany() == null ? Optional.empty() : companyService.findById(purchaseOrderReq.getCompany().getId());

        boolean first = purchaseOrderReq.getCompany() == null || (optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId));

        return first;
    }

}
