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
    private final ScheduleRepository scheduleRepository;

    public Schedule create(Schedule Schedule) {
        return scheduleRepository.save(Schedule);
    }

    public Schedule update(Schedule Schedule) {
        return scheduleRepository.save(Schedule);
    }

    public Collection<Schedule> getAll() { return scheduleRepository.findAll(); }

    public void delete(Long id){ scheduleRepository.deleteById(id);}

    public Optional<Schedule> findById(Long id) {return scheduleRepository.findById(id); }
}
