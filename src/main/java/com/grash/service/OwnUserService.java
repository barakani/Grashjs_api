package com.grash.service;

import com.grash.model.OwnUser;
import com.grash.repository.OwnUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnUserService {
    private final OwnUserRepository ownUserRepository;

    public Optional<OwnUser> findById(Long id) {return  ownUserRepository.findById(id); }

    public boolean isOwnUserInCompany(OwnUser ownUser, long companyId, boolean optional){
        if (optional){
            Optional<OwnUser> optionalOwnUser = ownUser == null ? Optional.empty() : findById(ownUser.getId());
            return ownUser == null || (optionalOwnUser.isPresent() && optionalOwnUser.get().getCompany().getId().equals(companyId));
        } else {
            Optional<OwnUser> optionalOwnUser = findById(ownUser.getId());
            return optionalOwnUser.isPresent() && optionalOwnUser.get().getCompany().getId().equals(companyId);
        }
    }
}
