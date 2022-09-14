package com.grash.repository;

import com.grash.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Collection<Vendor> findByCompany_Id(Long id);
}
