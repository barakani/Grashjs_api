package com.grash.service;

import com.grash.model.Task;
import com.grash.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task create(Task Task) {
        return taskRepository.save(Task);
    }

    public Task update(Task Task) {
        return taskRepository.save(Task);
    }

    public Collection<Task> getAll() { return taskRepository.findAll(); }

    public void delete(Long id){ taskRepository.deleteById(id);}

    public Optional<Task> findById(Long id) {return taskRepository.findById(id); }
}
