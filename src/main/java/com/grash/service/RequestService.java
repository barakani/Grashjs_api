package com.grash.service;

import com.grash.dto.RequestPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;
    private final CompanyService companyService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final UserService userService;
    private final TeamService teamService;
    private final AssetService assetService;

    public Request create(Request request) {
        return requestRepository.save(request);
    }

    public Request update(Long id, RequestPatchDTO request) {
        if (requestRepository.existsById(id)) {
            Request savedRequest = requestRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(request, savedRequest);
            return requestRepository.save(savedRequest);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Request> getAll() {
        return requestRepository.findAll();
    }

    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

    public Collection<Request> findByCompany(Long id) {
        return requestRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Request request) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(request.getCompany().getId());
    }

    public boolean canCreate(User user, Request requestReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(requestReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(requestReq, RequestPatchDTO.class));
    }

    public boolean canPatch(User user, RequestPatchDTO requestReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = requestReq.getLocation() == null ? Optional.empty() : locationService.findById(requestReq.getLocation().getId());
        Optional<Image> optionalImage = requestReq.getImage() == null ? Optional.empty() : imageService.findById(requestReq.getImage().getId());
        Optional<Asset> optionalAsset = requestReq.getAsset() == null ? Optional.empty() : assetService.findById(requestReq.getAsset().getId());
        Optional<User> optionalAssignedTo = requestReq.getAssignedTo() == null ? Optional.empty() : userService.findById(requestReq.getAssignedTo().getId());
        Optional<Team> optionalTeam = requestReq.getTeam() == null ? Optional.empty() : teamService.findById(requestReq.getTeam().getId());

        //optional fields
        boolean first = requestReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        boolean second = requestReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = requestReq.getImage() == null || (optionalImage.isPresent() && optionalImage.get().getCompany().getId().equals(companyId));
        boolean fourth = requestReq.getAssignedTo() == null || (optionalAssignedTo.isPresent() && optionalAssignedTo.get().getCompany().getId().equals(companyId));
        boolean fifth = requestReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));

        return first && second && third && fourth && fifth;
    }
}
