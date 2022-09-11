package com.grash.repository;

import com.grash.model.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<Checklist, Long> {
}
