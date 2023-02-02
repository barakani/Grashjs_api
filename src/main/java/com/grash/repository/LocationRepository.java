package com.grash.repository;

import com.grash.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Collection<Location> findByCompany_Id(Long id);

    Collection<Location> findByParentLocation_Id(Long id);

    Optional<Location> findByNameAndCompany_Id(String locationName, Long companyId);
}
