package com.grash.service;

import com.grash.model.Role;
import com.grash.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository RoleRepository;

    public Role create(Role Role) {
        return RoleRepository.save(Role);
    }

    public Role update(Role Role) {
        return RoleRepository.save(Role);
    }

    public Collection<Role> getAll() { return RoleRepository.findAll(); }

    public void delete(Long id){ RoleRepository.deleteById(id);}

    public Optional<Role> findById(Long id) {return RoleRepository.findById(id); }
}
