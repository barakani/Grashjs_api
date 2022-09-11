package com.grash.service;

import com.grash.model.Schedule;
import com.grash.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository ScheduleRepository;

    public Schedule create(Schedule Schedule) {
        return ScheduleRepository.save(Schedule);
    }

    public Schedule update(Schedule Schedule) {
        return ScheduleRepository.save(Schedule);
    }

    public Collection<Schedule> getAll() { return ScheduleRepository.findAll(); }

    public void delete(Long id){ ScheduleRepository.deleteById(id);}

    public Optional<Schedule> findById(Long id) {return ScheduleRepository.findById(id); }
}
