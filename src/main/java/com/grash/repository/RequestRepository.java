package com.grash.repository;

import com.grash.model.Relation;
import com.grash.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r from Request r where r.company.id = :x ")
    Collection<Request> findByCompany_Id(@Param("x") Long id);
}
