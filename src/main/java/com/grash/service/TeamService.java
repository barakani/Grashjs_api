package com.grash.service;

import com.grash.dto.TeamPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.TeamMapper;
import com.grash.model.Company;
import com.grash.model.Notification;
import com.grash.model.OwnUser;
import com.grash.model.Team;
import com.grash.model.enums.NotificationType;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.RoleType;
import com.grash.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CompanyService companyService;
    private final TeamMapper teamMapper;
    private final NotificationService notificationService;
    private final EntityManager em;

    @Transactional
    public Team create(Team team) {
        Team savedTeam = teamRepository.saveAndFlush(team);
        em.refresh(savedTeam);
        return savedTeam;
    }

    @Transactional
    public Team update(Long id, TeamPatchDTO team) {
        if (teamRepository.existsById(id)) {
            Team savedTeam = teamRepository.findById(id).get();
            Team updatedTeam = teamRepository.saveAndFlush(teamMapper.updateTeam(savedTeam, team));
            em.refresh(updatedTeam);
            return updatedTeam;
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Team> getAll() {
        return teamRepository.findAll();
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    public Collection<Team> findByCompany(Long id) {
        return teamRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(OwnUser user, Team team) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(team.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Team teamReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(teamReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        boolean second = user.getRole().getCreatePermissions().contains(PermissionEntity.PEOPLE_AND_TEAMS);

        return first && second && canPatch(user, teamMapper.toPatchDto(teamReq));
    }

    public boolean canPatch(OwnUser user, TeamPatchDTO teamReq) {
        return user.getRole().getCreatePermissions().contains(PermissionEntity.PEOPLE_AND_TEAMS);
    }

    public void notify(Team team) {
        String message = "You have been added to the " + team.getName() + " Team";
        if (team.getUsers() != null) {
            team.getUsers().forEach(assignedUser ->
                    notificationService.create(new Notification(message, assignedUser, NotificationType.TEAM, team.getId())));
        }
    }

    public void patchNotify(Team oldTeam, Team newTeam) {
        String message = "You have been added to the " + newTeam.getName() + " Team";
        if (newTeam.getUsers() != null) {
            List<OwnUser> newUsers = newTeam.getUsers().stream().filter(
                    user -> oldTeam.getUsers().stream().noneMatch(user1 -> user1.getId().equals(user.getId()))).collect(Collectors.toList());
            newUsers.forEach(newUser ->
                    notificationService.create(new Notification(message, newUser, NotificationType.TEAM, newTeam.getId())));
        }
    }
}
