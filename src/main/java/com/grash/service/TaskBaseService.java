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
    private final TaskBaseRepository TaskBaseRepository;

    public TaskBase create(TaskBase TaskBase) {
        return TaskBaseRepository.save(TaskBase);
    }

    public TaskBase update(TaskBase TaskBase) {
        return TaskBaseRepository.save(TaskBase);
    }

    public Collection<TaskBase> getAll() { return TaskBaseRepository.findAll(); }

    public void delete(Long id){ TaskBaseRepository.deleteById(id);}

    public Optional<TaskBase> findById(Long id) {return TaskBaseRepository.findById(id); }
}
