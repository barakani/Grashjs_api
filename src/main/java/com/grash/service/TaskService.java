package com.grash.service;

import com.grash.dto.TaskPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Task;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.model.enums.RoleType;
import com.grash.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final WorkOrderService workOrderService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public Task create(Task Task) {
        return taskRepository.save(Task);
    }

    public Task update(Long id, TaskPatchDTO task) {
        if (taskRepository.existsById(id)) {
            Task savedTask = taskRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(task, savedTask);
            return taskRepository.save(savedTask);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Task> getAll() {
        return taskRepository.findAll();
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public boolean hasAccess(User user, Task task) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(task.getCompany().getId());
    }

    public boolean canCreate(User user, Task taskReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(taskReq.getCompany().getId());
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(taskReq.getWorkOrder().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalWorkOrder.isPresent() && optionalWorkOrder.get().getCompany().getId().equals(companyId);

        return first && second && canPatch(user, modelMapper.map(taskReq, TaskPatchDTO.class));
    }

    public boolean canPatch(User user, TaskPatchDTO taskReq) {
        return true;
    }
}
