package com.grash.service;

import com.grash.advancedsearch.SearchCriteria;
import com.grash.advancedsearch.SpecificationBuilder;
import com.grash.dto.PartPatchDTO;
import com.grash.dto.PartShowDTO;
import com.grash.dto.imports.PartImportDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.PartMapper;
import com.grash.model.*;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.PartRepository;
import com.grash.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class PartService {
    private final PartRepository partRepository;
    private final FileService fileService;
    private final PartConsumptionService partConsumptionService;
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final VendorService vendorService;
    private final LocationService locationService;
    private final PartMapper partMapper;
    private final EntityManager em;
    private final NotificationService notificationService;
    private final UserService userService;
    private final TeamService teamService;


    @Transactional
    public Part create(Part Part) {
        Part savedPart = partRepository.saveAndFlush(Part);
        em.refresh(savedPart);
        return savedPart;
    }

    @Transactional
    public Part update(Long id, PartPatchDTO part) {
        if (partRepository.existsById(id)) {
            Part savedPart = partRepository.findById(id).get();
            Part patchedPart = partRepository.saveAndFlush(partMapper.updatePart(savedPart, part));
            em.refresh(patchedPart);
            return patchedPart;

        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public void consumePart(Long id, int quantity, Company company, WorkOrder workOrder) {
        Part part = findById(id).get();
        if (part.getQuantity() >= quantity) {
            part.setQuantity(part.getQuantity() - quantity);
            if (part.getQuantity() < part.getMinQuantity()) {
                part.getAssignedTo().forEach(user ->
                        notificationService.create(new Notification(part.getName() + " is getting Low", user, NotificationType.PART, part.getId()))
                );
            }
            partConsumptionService.create(new PartConsumption(company, part, workOrder, quantity));
            partRepository.save(part);
        } else throw new CustomException("There is not enough of this part", HttpStatus.NOT_ACCEPTABLE);
    }

    public Collection<Part> getAll() {
        return partRepository.findAll();
    }

    public void delete(Long id) {
        partRepository.deleteById(id);
    }

    public Optional<Part> findById(Long id) {
        return partRepository.findById(id);
    }

    public Collection<Part> findByCompany(Long id) {
        return partRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(OwnUser user, Part part) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(part.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Part partReq) {
        Long companyId = user.getCompany().getId();

        boolean first = companyService.isCompanyValid(partReq.getCompany(), companyId);
        return first && canPatch(user, partMapper.toPatchDto(partReq));
    }

    public boolean canPatch(OwnUser user, PartPatchDTO partReq) {
        Long companyId = user.getCompany().getId();
        boolean third = fileService.isFileInCompany(partReq.getImage(), companyId, true);
        boolean fourth = locationService.isLocationInCompany(partReq.getLocation(), companyId, true);
        return third && fourth;
    }

    public void notify(Part part) {
        String message = "Part " + part.getName() + " has been assigned to you";
        part.getUsers().forEach(user -> notificationService.create(notificationService.create(new Notification(message, user, NotificationType.PART, part.getId()))));
    }

    public void patchNotify(Part oldPart, Part newPart) {
        String message = "Part " + newPart.getName() + " has been assigned to you";
        oldPart.getNewUsersToNotify(newPart.getUsers()).forEach(user -> notificationService.create(
                new Notification(message, user, NotificationType.PART, newPart.getId())));
    }

    public Part save(Part part) {
        return partRepository.save(part);
    }

    public boolean isPartInCompany(Part part, long companyId, boolean optional) {
        if (optional) {
            Optional<Part> optionalPart = part == null ? Optional.empty() : findById(part.getId());
            return part == null || (optionalPart.isPresent() && optionalPart.get().getCompany().getId().equals(companyId));
        } else {
            Optional<Part> optionalPart = findById(part.getId());
            return optionalPart.isPresent() && optionalPart.get().getCompany().getId().equals(companyId);
        }
    }

    public Page<PartShowDTO> findBySearchCriteria(SearchCriteria searchCriteria) {
        SpecificationBuilder<Part> builder = new SpecificationBuilder<>();
        searchCriteria.getFilterFields().forEach(builder::with);
        Pageable page = PageRequest.of(searchCriteria.getPageNum(), searchCriteria.getPageSize(), searchCriteria.getDirection(), "id");
        return partRepository.findAll(builder.build(), page).map(partMapper::toShowDto);
    }

    public void importPart(Part part, PartImportDTO dto, Company company) {
        Long companyId = company.getId();
        Long companySettingsId = company.getCompanySettings().getId();
        part.setName(dto.getName());
        part.setCost(dto.getCost());
        part.setCategory(dto.getCategory());
        part.setNonStock(Helper.getBooleanFromString(dto.getCategory()));
        part.setBarcode(dto.getBarcode());
        part.setDescription(dto.getDescription());
        part.setQuantity(dto.getQuantity());
        part.setAdditionalInfos(dto.getAdditionalInfos());
        part.setArea(dto.getArea());
        part.setMinQuantity(dto.getMinQuantity());
//        Optional<Location> optionalLocation = locationService.findByNameAndCompany(dto.getLocationName(), companyId);
//        optionalLocation.ifPresent(part::setLocation);
        List<OwnUser> users = new ArrayList<>();
        dto.getAssignedToEmails().forEach(email -> {
            Optional<OwnUser> optionalUser1 = userService.findByEmailAndCompany(email, companyId);
            optionalUser1.ifPresent(users::add);
        });
        part.setAssignedTo(users);
        List<Team> teams = new ArrayList<>();
        dto.getTeamsNames().forEach(teamName -> {
            Optional<Team> optionalTeam = teamService.findByNameAndCompany(teamName, companyId);
            optionalTeam.ifPresent(teams::add);
        });
        part.setTeams(teams);
        List<Customer> customers = new ArrayList<>();
        dto.getCustomersNames().forEach(name -> {
            Optional<Customer> optionalCustomer = customerService.findByNameAndCompany(name, companyId);
            optionalCustomer.ifPresent(customers::add);
        });
        part.setCustomers(customers);
        List<Vendor> vendors = new ArrayList<>();
        dto.getVendorsNames().forEach(name -> {
            Optional<Vendor> optionalVendor = vendorService.findByNameAndCompany(name, companyId);
            optionalVendor.ifPresent(vendors::add);
        });
        part.setVendors(vendors);
        partRepository.save(part);
    }

    public Optional<Part> findByIdAndCompany(Long id, Long companyId) {
        return partRepository.findByIdAndCompany_Id(id, companyId);
    }
}
