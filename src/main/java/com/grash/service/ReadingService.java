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
    private final ReadingRepository readingRepository;

    public Reading create(Reading Reading) {
        return readingRepository.save(Reading);
    }

    public Reading update(Reading Reading) {
        return readingRepository.save(Reading);
    }

    public Collection<Reading> getAll() { return readingRepository.findAll(); }

    public void delete(Long id){ readingRepository.deleteById(id);}

    public Optional<Reading> findById(Long id) {return readingRepository.findById(id); }
}
