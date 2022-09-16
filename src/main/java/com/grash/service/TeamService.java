package com.grash.service;

import com.grash.dto.TeamPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Team;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public Team create(Team Team) {
        return teamRepository.save(Team);
    }

    public Team update(Long id, TeamPatchDTO team) {
        if (teamRepository.existsById(id)) {
            Team savedTeam = teamRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(team, savedTeam);
            return teamRepository.save(savedTeam);
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

    public boolean hasAccess(User user, Team team) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(team.getCompany().getId());
    }

    public boolean canCreate(User user, Team teamReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(teamReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(teamReq, TeamPatchDTO.class));
    }

    public boolean canPatch(User user, TeamPatchDTO teamReq) {
        return true;
    }
}
