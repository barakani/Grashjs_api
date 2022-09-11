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
    private final BillingInfosRepository BillingInfosRepository;

    private final ModelMapper modelMapper;

    public BillingInfos create(BillingInfos BillingInfos) {
        return BillingInfosRepository.save(BillingInfos);
    }

    public BillingInfos update(BillingInfos BillingInfos) {
        return BillingInfosRepository.save(BillingInfos);
    }

    public Collection<BillingInfos> getAll() { return BillingInfosRepository.findAll(); }

    public void delete(Long id){ BillingInfosRepository.deleteById(id);}

    public Optional<BillingInfos> findById(Long id) {return BillingInfosRepository.findById(id); }
}
