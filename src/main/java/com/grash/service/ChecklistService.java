package com.grash.service;

import com.grash.dto.ChecklistPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Checklist;
import com.grash.model.CompanySettings;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChecklistService {
    private final CheckListRepository checklistRepository;
    private final CompanySettingsService companySettingsService;

    private final ModelMapper modelMapper;

    public Checklist create(Checklist Checklist) {
        return checklistRepository.save(Checklist);
    }

    public Checklist update(Long id, ChecklistPatchDTO checklist) {
        if (checklistRepository.existsById(id)) {
            Checklist savedChecklist = checklistRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(checklist, savedChecklist);
            return checklistRepository.save(savedChecklist);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Checklist> getAll() {
        return checklistRepository.findAll();
    }

    public void delete(Long id) {
        checklistRepository.deleteById(id);
    }

    public Optional<Checklist> findById(Long id) {
        return checklistRepository.findById(id);
    }

    public Collection<Checklist> findByCompanySettings(Long id) {
        return checklistRepository.findByCompanySettings_Id(id);
    }

    public boolean hasAccess(User user, Checklist checklist) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(checklist.getCompanySettings().getCompany().getId());
    }

    public boolean canCreate(User user, Checklist checklistReq) {
        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(checklistReq.getCompanySettings().getId());

        //@NotNull fields
        boolean first = optionalCompanySettings.isPresent() && optionalCompanySettings.get().getId().equals(user.getCompany().getCompanySettings().getId());

        return first && canPatch(user, modelMapper.map(checklistReq, ChecklistPatchDTO.class));
    }

    public boolean canPatch(User user, ChecklistPatchDTO checklistReq) {
        return true;
    }
}
