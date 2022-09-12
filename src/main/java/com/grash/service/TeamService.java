package com.grash.service;

import com.grash.model.Team;
import com.grash.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public Team create(Team Team) {
        return teamRepository.save(Team);
    }

    public Team update(Team Team) {
        return teamRepository.save(Team);
    }

    public Collection<Team> getAll() { return teamRepository.findAll(); }

    public void delete(Long id){ teamRepository.deleteById(id);}

    public Optional<Team> findById(Long id) {return teamRepository.findById(id); }
}
