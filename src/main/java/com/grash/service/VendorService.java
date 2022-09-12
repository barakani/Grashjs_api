package com.grash.service;

import com.grash.model.Vendor;
import com.grash.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;

    public Vendor create(Vendor Vendor) {
        return vendorRepository.save(Vendor);
    }

    public Vendor update(Vendor Vendor) {
        return vendorRepository.save(Vendor);
    }

    public Collection<Vendor> getAll() { return vendorRepository.findAll(); }

    public void delete(Long id){ vendorRepository.deleteById(id);}

    public Optional<Vendor> findById(Long id) {return vendorRepository.findById(id); }
}
