package com.grash.service;

import com.grash.dto.LocationPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.LocationMapper;
import com.grash.model.Company;
import com.grash.model.Location;
import com.grash.model.Notification;
import com.grash.model.OwnUser;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final VendorService vendorService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final LocationMapper locationMapper;
    private final NotificationService notificationService;
    private final EntityManager em;

    @Transactional
    public Location create(Location location) {
        Location savedLocation = locationRepository.saveAndFlush(location);
        em.refresh(savedLocation);
        return savedLocation;
    }

    @Transactional
    public Location update(Long id, LocationPatchDTO location) {
        if (locationRepository.existsById(id)) {
            Location savedLocation = locationRepository.findById(id).get();
            Location patchedLocation = locationRepository.saveAndFlush(locationMapper.updateLocation(savedLocation, location));
            em.refresh(patchedLocation);
            return patchedLocation;
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Location> getAll() {
        return locationRepository.findAll();
    }

    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public Collection<Location> findByCompany(Long id) {
        return locationRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(OwnUser user, Location location) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(location.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Location locationReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(locationReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, locationMapper.toPatchDto(locationReq));
    }

    public boolean canPatch(OwnUser user, LocationPatchDTO locationReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = locationReq.getParentLocation() == null ? Optional.empty() : findById(locationReq.getParentLocation().getId());
        //optional fields
        boolean second = locationReq.getParentLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));

        return second;
    }

    public void notify(Location location) {
        String message = "Location " + location.getName() + " has been assigned to you";
        location.getUsers().forEach(user -> notificationService.create(new Notification(message, user, NotificationType.LOCATION, location.getId())));
    }

    public void patchNotify(Location oldLocation, Location newLocation) {
        String message = "Location " + newLocation.getName() + " has been assigned to you";
        oldLocation.getNewUsersToNotify(newLocation.getUsers()).forEach(user -> notificationService.create(
                new Notification(message, user, NotificationType.LOCATION, newLocation.getId())));
    }

    public Collection<Location> findLocationChildren(Long id) {
        return locationRepository.findByParentLocation_Id(id);
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public boolean isLocationInCompany(Location location, long companyId, boolean optional) {
        if (optional) {
            Optional<Location> optionalLocation = location == null ? Optional.empty() : findById(location.getId());
            return location == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        } else {
            Optional<Location> optionalLocation = findById(location.getId());
            return optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);
        }
    }
}
