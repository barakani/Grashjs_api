package com.grash.service;

import com.grash.model.TaskBase;
import com.grash.repository.TaskBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskBaseService {
    private final TaskBaseRepository taskBaseRepository;

    public TaskBase create(TaskBase TaskBase) {
        return taskBaseRepository.save(TaskBase);
    }

    public TaskBase update(TaskBase TaskBase) {
        return taskBaseRepository.save(TaskBase);
    }

    public Collection<TaskBase> getAll() { return taskBaseRepository.findAll(); }

    public void delete(Long id){ taskBaseRepository.deleteById(id);}

    public Optional<TaskBase> findById(Long id) {return taskBaseRepository.findById(id); }
}
