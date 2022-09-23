package com.grash.repository;

import com.grash.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findByCompany_Id(@Param("x") Long id);
}
