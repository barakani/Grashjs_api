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
    private final VendorRepository VendorRepository;

    public Vendor create(Vendor Vendor) {
        return VendorRepository.save(Vendor);
    }

    public Vendor update(Vendor Vendor) {
        return VendorRepository.save(Vendor);
    }

    public Collection<Vendor> getAll() { return VendorRepository.findAll(); }

    public void delete(Long id){ VendorRepository.deleteById(id);}

    public Optional<Vendor> findById(Long id) {return VendorRepository.findById(id); }
}
