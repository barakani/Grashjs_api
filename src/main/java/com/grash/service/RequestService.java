package com.grash.service;

import com.grash.dto.RequestPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.RequestMapper;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final CompanyService companyService;
    private final FileService fileService;
    private final LocationService locationService;
    private final UserService userService;
    private final TeamService teamService;
    private final AssetService assetService;
    private final WorkOrderService workOrderService;
    private final RequestMapper requestMapper;
    private final EntityManager em;

    @Transactional
    public Request create(Request request) {
        Request savedRequest = requestRepository.saveAndFlush(request);
        em.refresh(savedRequest);
        return savedRequest;
    }

    @Transactional
    public Request update(Long id, RequestPatchDTO request) {
        if (requestRepository.existsById(id)) {
            Request savedRequest = requestRepository.findById(id).get();
            Request updatedRequest = requestRepository.saveAndFlush(requestMapper.updateRequest(savedRequest, request));
            em.refresh(updatedRequest);
            return updatedRequest;
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

    public boolean hasAccess(OwnUser user, Request request) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(request.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Request requestReq) {
        Long companyId = user.getCompany().getId();
        //@NotNull fields
        boolean first = companyService.isCompanyValid(requestReq.getCompany(), companyId);
        return first && canPatch(user, requestMapper.toPatchDto(requestReq));
    }

    public boolean canPatch(OwnUser user, RequestPatchDTO requestReq) {
        Long companyId = user.getCompany().getId();

        Optional<Location> optionalLocation = requestReq.getLocation() == null ? Optional.empty() : locationService.findById(requestReq.getLocation().getId());
        Optional<File> optionalImage = requestReq.getImage() == null ? Optional.empty() : fileService.findById(requestReq.getImage().getId());
        Optional<Asset> optionalAsset = requestReq.getAsset() == null ? Optional.empty() : assetService.findById(requestReq.getAsset().getId());
        Optional<OwnUser> optionalPrimaryUser = requestReq.getPrimaryUser() == null ? Optional.empty() : userService.findById(requestReq.getPrimaryUser().getId());
        Optional<Team> optionalTeam = requestReq.getTeam() == null ? Optional.empty() : teamService.findById(requestReq.getTeam().getId());

        //optional fields
        boolean first = requestReq.getAsset() == null || (optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId));
        boolean second = requestReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));
        boolean third = requestReq.getImage() == null || (optionalImage.isPresent() && optionalImage.get().getCompany().getId().equals(companyId));
        boolean fourth = requestReq.getPrimaryUser() == null || (optionalPrimaryUser.isPresent() && optionalPrimaryUser.get().getCompany().getId().equals(companyId));
        boolean fifth = requestReq.getTeam() == null || (optionalTeam.isPresent() && optionalTeam.get().getCompany().getId().equals(companyId));

        return first && second && third && fourth && fifth;
    }

    public WorkOrder createWorkOrderFromRequest(Request request, OwnUser creator) {
        WorkOrder workOrder = workOrderService.getWorkOrderFromWorkOrderBase(request);
        if (creator.getCompany().getCompanySettings().getGeneralPreferences().isAutoAssignRequests()) {
            OwnUser primaryUser = workOrder.getPrimaryUser();
            workOrder.setPrimaryUser(primaryUser == null ? creator : primaryUser);
        }
        workOrder.setParentRequest(request);
        WorkOrder savedWorkOrder = workOrderService.create(workOrder);
        workOrderService.notify(savedWorkOrder);
        request.setWorkOrder(savedWorkOrder);
        requestRepository.save(request);

        return savedWorkOrder;
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Collection<Request> findByCreatedAtBetweenAndCompany(Date date1, Date date2, Long id) {
        return requestRepository.findByCreatedAtBetweenAndCompany_Id(date1.toInstant(), date2.toInstant(), id);
    }
}
