package com.grash.repository;

import com.grash.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    Collection<Team> findByCompany_Id(Long id);

    Collection<Team> findByUsers_Id(Long id);
}
