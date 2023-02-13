package com.grash.service;

import com.grash.dto.WorkflowPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.WorkflowMapper;
import com.grash.model.OwnUser;
import com.grash.model.Workflow;
import com.grash.model.enums.RoleType;
import com.grash.model.enums.WFMainCondition;
import com.grash.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkflowService {
    private final WorkflowRepository workflowRepository;
    private final WorkflowMapper workflowMapper;

    public Workflow create(Workflow Workflow) {
        return workflowRepository.save(Workflow);
    }

    public Workflow update(Long id, WorkflowPatchDTO workflowsPatchDTO) {
        if (workflowRepository.existsById(id)) {
            Workflow savedWorkflow = workflowRepository.findById(id).get();
            return workflowRepository.save(workflowMapper.updateWorkflow(savedWorkflow, workflowsPatchDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Workflow> getAll() {
        return workflowRepository.findAll();
    }

    public void delete(Long id) {
        workflowRepository.deleteById(id);
    }

    public Optional<Workflow> findById(Long id) {
        return workflowRepository.findById(id);
    }

    public Collection<Workflow> findByMainConditionAndCompany(WFMainCondition mainCondition, Long id) {
        return workflowRepository.findByMainConditionAndCompany_Id(mainCondition, id);
    }

    public boolean hasAccess(OwnUser user, Workflow workflow) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(workflow.getCompany().getId());
    }

    public boolean canPatch(OwnUser user, WorkflowPatchDTO workflow) {
        return true;
    }

    public Collection<Workflow> findByCompany(Long id) {
        return workflowRepository.findByCompany_Id(id);
    }
}
