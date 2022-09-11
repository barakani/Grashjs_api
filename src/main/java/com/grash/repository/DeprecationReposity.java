package com.grash.repository;

import com.grash.model.Deprecation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeprecationReposity extends JpaRepository<Deprecation, Long> {
}
