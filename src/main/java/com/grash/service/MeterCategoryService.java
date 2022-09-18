package com.grash.service;

import com.grash.dto.MeterCategoryPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.CompanySettings;
import com.grash.model.MeterCategory;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.MeterCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterCategoryService {
    private final MeterCategoryRepository meterCategoryRepository;

    private final CompanySettingsService companySettingsService;

    private final ModelMapper modelMapper;

    public MeterCategory create(MeterCategory MeterCategory) {
        return meterCategoryRepository.save(MeterCategory);
    }

    public MeterCategory update(Long id, MeterCategoryPatchDTO meterCategory) {
        if (meterCategoryRepository.existsById(id)) {
            MeterCategory savedMeterCategory = meterCategoryRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(meterCategory, savedMeterCategory);
            return meterCategoryRepository.save(savedMeterCategory);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<MeterCategory> getAll() { return meterCategoryRepository.findAll(); }

    public void delete(Long id){ meterCategoryRepository.deleteById(id);}

    public Optional<MeterCategory> findById(Long id) {return meterCategoryRepository.findById(id); }

    public Collection<MeterCategory> findByCompany(Long id) {
        return meterCategoryRepository.findByCompany_Id(id);
    }
    public boolean hasAccess(User user, MeterCategory meterCategory) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(meterCategory.getCompanySettings().getCompany().getId());
    }

    public boolean canCreate(User user, MeterCategory meterCategoryReq) {
        Long companyId = user.getCompany().getId();

        Optional<CompanySettings> optionalCompanySettings = companySettingsService.findById(meterCategoryReq.getCompanySettings().getId());

        boolean first = optionalCompanySettings.isPresent() && optionalCompanySettings.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(meterCategoryReq, MeterCategoryPatchDTO.class));
    }

    public boolean canPatch(User user, MeterCategoryPatchDTO meterCategoryReq) {
        Long companyId = user.getCompany().getId();

        Optional<CompanySettings> optionalCompanySettings = meterCategoryReq.getCompanySettings() == null ? Optional.empty() : companySettingsService.findById(meterCategoryReq.getCompanySettings().getId());

        boolean first = meterCategoryReq.getCompanySettings() == null || (optionalCompanySettings.isPresent() && optionalCompanySettings.get().getCompany().getId().equals(companyId));

        return first;
    }


}
