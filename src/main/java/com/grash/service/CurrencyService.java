package com.grash.service;

import com.grash.model.Currency;
import com.grash.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository CurrencyRepository;

    private final ModelMapper modelMapper;

    public Currency create(Currency Currency) {
        return CurrencyRepository.save(Currency);
    }

    public Currency update(Currency Currency) {
        return CurrencyRepository.save(Currency);
    }

    public Collection<Currency> getAll() { return CurrencyRepository.findAll(); }

    public void delete(Long id){ CurrencyRepository.deleteById(id);}

    public Optional<Currency> findById(Long id) {return CurrencyRepository.findById(id); }
}
