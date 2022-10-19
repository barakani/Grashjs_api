package com.grash.service;

import com.grash.dto.PartPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.PartMapper;
import com.grash.model.*;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.RoleType;
import com.grash.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;
    private final ImageService imageService;
    private final AssetService assetService;
    private final CompanyService companyService;
    private final LocationService locationService;
    private final PartMapper partMapper;

    private final NotificationService notificationService;

    public Part create(Part Part) {
        return partRepository.save(Part);
    }

    public Part update(Long id, PartPatchDTO part) {
        if (partRepository.existsById(id)) {
            Part savedPart = partRepository.findById(id).get();
            return partRepository.save(partMapper.updatePart(savedPart, part));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
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

    public boolean hasAccess(User user, Part part) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(part.getCompany().getId());
    }

    public boolean canCreate(User user, Part partReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(partReq.getCompany().getId());
        Optional<Asset> optionalAsset = assetService.findById(partReq.getAsset().getId());
        Optional<Location> optionalLocation = locationService.findById(partReq.getLocation().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId);
        boolean third = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);

        return first && second && third && canPatch(user, partMapper.toDto(partReq));
    }

    public boolean canPatch(User user, PartPatchDTO partReq) {
        Long companyId = user.getCompany().getId();

        Optional<Asset> optionalAsset = partReq.getAsset() == null ? Optional.empty() : assetService.findById(partReq.getAsset().getId());
        Optional<Image> optionalImage = partReq.getImage() == null ? Optional.empty() : imageService.findById(partReq.getImage().getId());
        Optional<Location> optionalLocation = partReq.getLocation() == null ? Optional.empty() : locationService.findById(partReq.getLocation().getId());

        boolean second = partReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        boolean third = partReq.getImage() == null || (optionalImage.isPresent() && optionalImage.get().getCompany().getId().equals(companyId));
        boolean fourth = partReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));

        return second && third && fourth;
    }

    public void notify(Part part) {

        String message = "Part " + part.getName() + " has been assigned to you";
        if (part.getAssignedTo() != null) {
            part.getAssignedTo().forEach(assignedUser ->
                    notificationService.create(new Notification(message, assignedUser, NotificationType.PART, part.getId())));
        }
        if (part.getTeams() != null) {
            part.getTeams().forEach(team -> team.getUsers().forEach(user ->
                    notificationService.create(new Notification(message, user, NotificationType.PART, part.getId()))));
        }
    }

    public void patchNotify(Part oldPart, Part newPart) {
        String message = "Part " + newPart.getName() + " has been assigned to you";
        if (newPart.getAssignedTo() != null) {
            List<User> newUsers = newPart.getAssignedTo().stream().filter(
                    user -> oldPart.getAssignedTo().stream().noneMatch(user1 -> user1.getId().equals(user.getId()))).collect(Collectors.toList());
            newUsers.forEach(newUser ->
                    notificationService.create(new Notification(message, newUser, NotificationType.ASSET, newPart.getId())));
        }
        if (newPart.getTeams() != null) {
            List<Team> newTeams = newPart.getTeams().stream().filter(
                    team -> oldPart.getTeams().stream().noneMatch(team1 -> team1.getId().equals(team.getId()))).collect(Collectors.toList());
            newTeams.forEach(team -> team.getUsers().forEach(user ->
                    notificationService.create(new Notification(message, user, NotificationType.ASSET, newPart.getId()))));
        }
    }
}
