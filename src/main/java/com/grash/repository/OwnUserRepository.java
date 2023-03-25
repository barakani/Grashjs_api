package com.grash.repository;

import com.grash.model.OwnUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface OwnUserRepository extends JpaRepository<OwnUser, Long>, JpaSpecificationExecutor<OwnUser> {

}
