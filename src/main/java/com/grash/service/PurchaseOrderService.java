package com.grash.service;

import com.grash.dto.PurchaseOrderPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.PurchaseOrderMapper;
import com.grash.model.Company;
import com.grash.model.OwnUser;
import com.grash.model.PurchaseOrder;
import com.grash.model.enums.RoleType;
import com.grash.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final CompanyService companyService;
    private final EntityManager em;

    @Transactional
    public PurchaseOrder create(PurchaseOrder purchaseOrder) {
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);
        em.refresh(savedPurchaseOrder);
        return savedPurchaseOrder;
    }

    @Transactional
    public PurchaseOrder update(Long id, PurchaseOrderPatchDTO purchaseOrder) {
        if (purchaseOrderRepository.existsById(id)) {
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.findById(id).get();
            PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.save(purchaseOrderMapper.updatePurchaseOrder(savedPurchaseOrder, purchaseOrder));
            em.refresh(updatedPurchaseOrder);
            return updatedPurchaseOrder;
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

    public boolean hasAccess(OwnUser user, PurchaseOrder purchaseOrder) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(purchaseOrder.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, PurchaseOrder purchaseOrderReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(purchaseOrderReq.getCompany().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, purchaseOrderMapper.toDto(purchaseOrderReq));
    }

    public boolean canPatch(OwnUser user, PurchaseOrderPatchDTO purchaseOrderReq) {
        return true;
    }

}
