package com.grash.service;

import com.grash.model.BankCard;
import com.grash.repository.BankCardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankCardService {
    private final BankCardRepository bankCardRepository;

    private final ModelMapper modelMapper;

    public BankCard create(BankCard BankCard) {
        return bankCardRepository.save(BankCard);
    }

    public BankCard update(BankCard BankCard) {
        return bankCardRepository.save(BankCard);
    }

    public Collection<BankCard> getAll() { return bankCardRepository.findAll(); }

    public void delete(Long id){ bankCardRepository.deleteById(id);}

    public Optional<BankCard> findById(Long id) {return bankCardRepository.findById(id); }
}
