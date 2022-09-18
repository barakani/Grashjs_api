package com.grash.service;

import com.grash.dto.LocationPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.LocationMapper;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public Location create(Location Location) {
        return locationRepository.save(Location);
    }

    public Location update(Long id, LocationPatchDTO location) {
        if (locationRepository.existsById(id)) {
            Location savedLocation = locationRepository.findById(id).get();
            return locationRepository.save(locationMapper.updateLocation(savedLocation, location));
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

    public boolean hasAccess(User user, Location location) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(location.getCompany().getId());
    }

    public boolean canCreate(User user, Location locationReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(locationReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, locationMapper.toDto(locationReq));
    }

    public boolean canPatch(User user, LocationPatchDTO locationReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = locationReq.getParentLocation() == null ? Optional.empty() : findById(locationReq.getParentLocation().getId());
        Optional<Vendor> optionalVendor = locationReq.getVendor() == null ? Optional.empty() : vendorService.findById(locationReq.getVendor().getId());
        Optional<Customer> optionalCustomer = locationReq.getCustomer() == null ? Optional.empty() : customerService.findById(locationReq.getCustomer().getId());

        //optional fields
        boolean second = locationReq.getParentLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = locationReq.getVendor() == null || (optionalVendor.isPresent() && optionalVendor.get().getCompany().getId().equals(companyId));
        boolean fourth = locationReq.getCustomer() == null || (optionalCustomer.isPresent() && optionalCustomer.get().getCompany().getId().equals(companyId));

        return second && third && fourth;
    }
}
