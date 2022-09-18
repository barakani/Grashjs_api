package com.grash.service;

import com.grash.dto.TaskBasePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.TaskBaseMapper;
import com.grash.model.Company;
import com.grash.model.TaskBase;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.TaskBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskBaseService {
    private final TaskBaseRepository taskBaseRepository;
    private final CompanyService companyService;
    private final TaskBaseMapper taskBaseMapper;

    public TaskBase create(TaskBase TaskBase) {
        return taskBaseRepository.save(TaskBase);
    }

    public TaskBase update(Long id, TaskBasePatchDTO taskBase) {
        if (taskBaseRepository.existsById(id)) {
            TaskBase savedTaskBase = taskBaseRepository.findById(id).get();
            return taskBaseRepository.save(taskBaseMapper.updateTaskBase(savedTaskBase, taskBase));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<TaskBase> getAll() {
        return taskBaseRepository.findAll();
    }

    public void delete(Long id) {
        taskBaseRepository.deleteById(id);
    }

    public Optional<TaskBase> findById(Long id) {
        return taskBaseRepository.findById(id);
    }

    public boolean hasAccess(User user, TaskBase taskBase) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(taskBase.getCompany().getId());
    }

    public boolean canCreate(User user, TaskBase taskBaseReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(taskBaseReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, taskBaseMapper.toDto(taskBaseReq));
    }

    public boolean canPatch(User user, TaskBasePatchDTO taskBaseReq) {
        return true;
    }
}
