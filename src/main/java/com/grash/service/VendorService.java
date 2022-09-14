package com.grash.service;

import com.grash.dto.VendorPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Vendor;
import com.grash.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;

    private final ModelMapper modelMapper;

    public Vendor create(Vendor Vendor) {
        return vendorRepository.save(Vendor);
    }

    public Vendor update(Long id, VendorPatchDTO vendor) {
        if (vendorRepository.existsById(id)) {
            Vendor savedVendor = vendorRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(vendor, savedVendor);
            return vendorRepository.save(savedVendor);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    public Optional<Vendor> findById(Long id) {
        return vendorRepository.findById(id);
    }

    public Collection<Vendor> findByCompany(Long id) {
        return vendorRepository.findByCompany_Id(id);
    }
}
