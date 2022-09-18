package com.grash.repository;

import com.grash.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PartRepository extends JpaRepository<Part, Long> {
    @Query("SELECT p from Part p where p.company.id = :x ")
    Collection<Part> findByCompany_Id(@Param("x") Long id);
}
