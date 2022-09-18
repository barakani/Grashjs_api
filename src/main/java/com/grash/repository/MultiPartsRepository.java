package com.grash.repository;

import com.grash.model.MultiParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface MultiPartsRepository extends JpaRepository<MultiParts, Long> {
    @Query("SELECT m from MultiParts m where m.company.id = :x ")
    Collection<MultiParts> findByCompany_Id(@Param("x") Long id);
}
