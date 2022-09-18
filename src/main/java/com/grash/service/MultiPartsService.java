package com.grash.service;

import com.grash.dto.MultiPartsPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.MultiPartsMapper;
import com.grash.model.Company;
import com.grash.model.MultiParts;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.MultiPartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiPartsService {
    private final MultiPartsRepository multiPartsRepository;
    private final CompanyService companyService;
    private final MultiPartsMapper multiPartsMapper;


    public MultiParts create(MultiParts MultiParts) {
        return multiPartsRepository.save(MultiParts);
    }

    public MultiParts update(Long id, MultiPartsPatchDTO multiPartsPatchDTO) {
        if (multiPartsRepository.existsById(id)) {
            MultiParts savedMultiParts = multiPartsRepository.findById(id).get();
            return multiPartsRepository.save(multiPartsMapper.updateMultiParts(savedMultiParts, multiPartsPatchDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<MultiParts> getAll() {
        return multiPartsRepository.findAll();
    }

    public void delete(Long id) {
        multiPartsRepository.deleteById(id);
    }

    public Optional<MultiParts> findById(Long id) {
        return multiPartsRepository.findById(id);
    }

    public Collection<MultiParts> findByCompany(Long id) {
        return multiPartsRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, MultiParts multiParts) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(multiParts.getCompany().getId());
    }

    public boolean canCreate(User user, MultiParts multiPartsReq) {
        Long companyId = user.getCompany().getId();
        Optional<Company> optionalCompany = companyService.findById(multiPartsReq.getCompany().getId());

        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, multiPartsMapper.toDto(multiPartsReq));
    }

    public boolean canPatch(User user, MultiPartsPatchDTO multiPartsReq) {
        return true;
    }
}
