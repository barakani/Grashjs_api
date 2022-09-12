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
    private final RoleRepository roleRepository;

    public Role create(Role Role) {
        return roleRepository.save(Role);
    }

    public Role update(Role Role) {
        return roleRepository.save(Role);
    }

    public Collection<Role> getAll() { return roleRepository.findAll(); }

    public void delete(Long id){ roleRepository.deleteById(id);}

    public Optional<Role> findById(Long id) {return roleRepository.findById(id); }
}
