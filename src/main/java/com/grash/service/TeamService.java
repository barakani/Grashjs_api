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
    private final TeamRepository TeamRepository;

    public Team create(Team Team) {
        return TeamRepository.save(Team);
    }

    public Team update(Team Team) {
        return TeamRepository.save(Team);
    }

    public Collection<Team> getAll() { return TeamRepository.findAll(); }

    public void delete(Long id){ TeamRepository.deleteById(id);}

    public Optional<Team> findById(Long id) {return TeamRepository.findById(id); }
}
