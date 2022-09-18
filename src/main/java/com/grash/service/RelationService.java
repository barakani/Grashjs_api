package com.grash.service;

import com.grash.dto.RelationPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.RelationMapper;
import com.grash.model.Relation;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.enums.RoleType;
import com.grash.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {
    private final RelationRepository relationRepository;
    private final WorkOrderService workOrderService;
    private final RelationMapper relationMapper;

    public Relation create(Relation Relation) {
        return relationRepository.save(Relation);
    }

    public Relation update(Long id, RelationPatchDTO relation) {
        if (relationRepository.existsById(id)) {
            Relation savedRelation = relationRepository.findById(id).get();
            return relationRepository.save(relationMapper.updateRelation(savedRelation, relation));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Relation> getAll() {
        return relationRepository.findAll();
    }

    public void delete(Long id) {
        relationRepository.deleteById(id);
    }

    public Optional<Relation> findById(Long id) {
        return relationRepository.findById(id);
    }

    public Collection<Relation> findByCompany(Long id) {
        return relationRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Relation relation) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(relation.getChild().getCompany().getId());
    }

    public boolean canCreate(User user, Relation relationReq) {

        return canPatch(user, relationMapper.toDto(relationReq));
    }

    public boolean canPatch(User user, RelationPatchDTO relationReq) {
        Long companyId = user.getCompany().getId();

        Optional<WorkOrder> optionalParentWorkOrder = relationReq.getParent() == null ? Optional.empty() : workOrderService.findById(relationReq.getParent().getId());
        Optional<WorkOrder> optionalChildWorkOrder = relationReq.getChild() == null ? Optional.empty() : workOrderService.findById(relationReq.getChild().getId());

        boolean first = relationReq.getParent() == null || (optionalParentWorkOrder.isPresent() && optionalParentWorkOrder.get().getCompany().getId().equals(companyId));
        boolean second = relationReq.getChild() == null || (optionalChildWorkOrder.isPresent() && optionalChildWorkOrder.get().getCompany().getId().equals(companyId));

        return first && second;
    }

}
