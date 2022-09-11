package com.grash.service;

import com.grash.model.Reading;
import com.grash.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingService {
    private final ReadingRepository ReadingRepository;

    public Reading create(Reading Reading) {
        return ReadingRepository.save(Reading);
    }

    public Reading update(Reading Reading) {
        return ReadingRepository.save(Reading);
    }

    public Collection<Reading> getAll() { return ReadingRepository.findAll(); }

    public void delete(Long id){ ReadingRepository.deleteById(id);}

    public Optional<Reading> findById(Long id) {return ReadingRepository.findById(id); }
}
