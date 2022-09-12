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
    private final MeterRepository meterRepository;

    public Meter create(Meter Meter) {
        return meterRepository.save(Meter);
    }

    public Meter update(Meter Meter) {
        return meterRepository.save(Meter);
    }

    public Collection<Meter> getAll() { return meterRepository.findAll(); }

    public void delete(Long id){ meterRepository.deleteById(id);}

    public Optional<Meter> findById(Long id) {return meterRepository.findById(id); }
}
