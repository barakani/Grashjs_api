package com.grash.repository;

import com.grash.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Collection<Notification> findByUser_Id(Long id);
}
