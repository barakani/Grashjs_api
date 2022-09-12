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
    private final CurrencyRepository currencyRepository;

    private final ModelMapper modelMapper;

    public Currency create(Currency Currency) {
        return currencyRepository.save(Currency);
    }

    public Currency update(Currency Currency) {
        return currencyRepository.save(Currency);
    }

    public Collection<Currency> getAll() { return currencyRepository.findAll(); }

    public void delete(Long id){ currencyRepository.deleteById(id);}

    public Optional<Currency> findById(Long id) {return currencyRepository.findById(id); }
}
