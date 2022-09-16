package com.grash.service;

import com.grash.dto.FloorPlanPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.FloorPlan;
import com.grash.model.Image;
import com.grash.model.Location;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.FloorPlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FloorPlanService {
    private final FloorPlanRepository floorPlanRepository;
    private final ImageService imageService;
    private final LocationService locationService;

    private final ModelMapper modelMapper;

    public FloorPlan create(FloorPlan FloorPlan) {
        return floorPlanRepository.save(FloorPlan);
    }

    public FloorPlan update(Long id, FloorPlanPatchDTO floorPlan) {
        if (floorPlanRepository.existsById(id)) {
            FloorPlan savedFloorPlan = floorPlanRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(floorPlan, savedFloorPlan);
            return floorPlanRepository.save(savedFloorPlan);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<FloorPlan> getAll() {
        return floorPlanRepository.findAll();
    }

    public void delete(Long id) {
        floorPlanRepository.deleteById(id);
    }

    public Optional<FloorPlan> findById(Long id) {
        return floorPlanRepository.findById(id);
    }

    public boolean hasAccess(User user, FloorPlan floorPlan) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(floorPlan.getLocation().getCompany().getId());
    }

    public boolean canCreate(User user, FloorPlan floorPlanReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = locationService.findById(floorPlanReq.getLocation().getId());

        //@NotNull fields
        boolean first = optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(floorPlanReq, FloorPlanPatchDTO.class));
    }

    public boolean canPatch(User user, FloorPlanPatchDTO floorPlanReq) {
        Long companyId = user.getCompany().getId();
        Optional<Image> optionalImage = floorPlanReq.getImage() == null ? Optional.empty() : imageService.findById(floorPlanReq.getImage().getId());

        //optional fields
        boolean third = !optionalImage.isPresent() || optionalImage.get().getCompany().getId().equals(companyId);

        if (third) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }
}
