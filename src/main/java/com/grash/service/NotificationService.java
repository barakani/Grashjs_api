package com.grash.service;

import com.grash.dto.NotificationPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.NotificationMapper;
import com.grash.model.Notification;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public Notification create(Notification Notification) {
        return notificationRepository.save(Notification);
    }

    public Notification update(Long id, NotificationPatchDTO notificationsPatchDTO) {
        if (notificationRepository.existsById(id)) {
            Notification savedNotification = notificationRepository.findById(id).get();
            return notificationRepository.save(notificationMapper.updateNotification(savedNotification, notificationsPatchDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    public Collection<Notification> findByUser(Long id) {
        return notificationRepository.findByUser_Id(id);
    }

    public boolean hasAccess(OwnUser user, Notification notification) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getId().equals(notification.getUser().getId());
    }

    public boolean canPatch(OwnUser user, NotificationPatchDTO notification) {
        return true;
    }
}
