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
    private final TaskRepository TaskRepository;

    public Task create(Task Task) {
        return TaskRepository.save(Task);
    }

    public Task update(Task Task) {
        return TaskRepository.save(Task);
    }

    public Collection<Task> getAll() { return TaskRepository.findAll(); }

    public void delete(Long id){ TaskRepository.deleteById(id);}

    public Optional<Task> findById(Long id) {return TaskRepository.findById(id); }
}
