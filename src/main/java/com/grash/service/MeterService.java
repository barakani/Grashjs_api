package com.grash.service;

import com.grash.model.Meter;
import com.grash.repository.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterService {
    private final MeterRepository MeterRepository;

    public Meter create(Meter Meter) {
        return MeterRepository.save(Meter);
    }

    public Meter update(Meter Meter) {
        return MeterRepository.save(Meter);
    }

    public Collection<Meter> getAll() { return MeterRepository.findAll(); }

    public void delete(Long id){ MeterRepository.deleteById(id);}

    public Optional<Meter> findById(Long id) {return MeterRepository.findById(id); }
}
