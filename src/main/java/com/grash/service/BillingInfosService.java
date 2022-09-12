package com.grash.service;

import com.grash.model.BillingInfos;
import com.grash.repository.BillingInfosRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingInfosService {
    private final BillingInfosRepository billingInfosRepository;

    private final ModelMapper modelMapper;

    public BillingInfos create(BillingInfos BillingInfos) {
        return billingInfosRepository.save(BillingInfos);
    }

    public BillingInfos update(BillingInfos BillingInfos) {
        return billingInfosRepository.save(BillingInfos);
    }

    public Collection<BillingInfos> getAll() { return billingInfosRepository.findAll(); }

    public void delete(Long id){ billingInfosRepository.deleteById(id);}

    public Optional<BillingInfos> findById(Long id) {return billingInfosRepository.findById(id); }
}
