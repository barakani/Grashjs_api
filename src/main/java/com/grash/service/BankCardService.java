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
    private final BankCardRepository BankCardRepository;

    private final ModelMapper modelMapper;

    public BankCard create(BankCard BankCard) {
        return BankCardRepository.save(BankCard);
    }

    public BankCard update(BankCard BankCard) {
        return BankCardRepository.save(BankCard);
    }

    public Collection<BankCard> getAll() { return BankCardRepository.findAll(); }

    public void delete(Long id){ BankCardRepository.deleteById(id);}

    public Optional<BankCard> findById(Long id) {return BankCardRepository.findById(id); }
}
