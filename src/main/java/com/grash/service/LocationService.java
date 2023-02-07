package com.grash.service;

import com.grash.dto.LocationPatchDTO;
import com.grash.dto.imports.LocationImportDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.LocationMapper;
import com.grash.model.*;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final CompanyService companyService;
    private final LocationMapper locationMapper;
    private final NotificationService notificationService;
    private final TeamService teamService;
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
        //@NotNull fields
        boolean first = companyService.isCompanyValid(locationReq.getCompany(), companyId);
        return first && canPatch(user, locationMapper.toPatchDto(locationReq));
    }

    public boolean canPatch(OwnUser user, LocationPatchDTO locationReq) {
        Long companyId = user.getCompany().getId();
        return isLocationInCompany(locationReq.getParentLocation(), companyId, true);
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

    public Optional<Location> findByNameAndCompany(String locationName, Long companyId) {
        return locationRepository.findByNameAndCompany_Id(locationName, companyId);
    }

    public void importLocation(Location location, LocationImportDTO dto, Company company) {
        Long companyId = company.getId();
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setLongitude(dto.getLongitude());
        location.setLatitude(dto.getLatitude());
        Optional<Location> optionalLocation = locationService.findByNameAndCompany(dto.getParentLocationName(), companyId);
        optionalLocation.ifPresent(location::setParentLocation);
        List<OwnUser> workers = new ArrayList<>();
        dto.getWorkersEmails().forEach(email -> {
            Optional<OwnUser> optionalUser1 = userService.findByEmailAndCompany(email, companyId);
            optionalUser1.ifPresent(workers::add);
        });
        location.setWorkers(workers);
        List<Team> teams = new ArrayList<>();
        dto.getTeamsNames().forEach(teamName -> {
            Optional<Team> optionalTeam = teamService.findByNameAndCompany(teamName, companyId);
            optionalTeam.ifPresent(teams::add);
        });
        location.setTeams(teams);
        locationRepository.save(location);
    }
}
