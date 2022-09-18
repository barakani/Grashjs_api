package com.grash.service;

import com.grash.dto.MeterPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.MeterMapper;
import com.grash.model.*;
import com.grash.model.enums.RoleType;
import com.grash.repository.MeterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterService {
    private final MeterRepository meterRepository;
    private final MeterCategoryService meterCategoryService;
    private final ImageService imageService;
    private final AssetService assetService;
    private final CompanyService companyService;
    private final LocationService locationService;

    private final MeterMapper meterMapper;

    public Meter create(Meter Meter) {
        return meterRepository.save(Meter);
    }

    public Meter update(Long id, MeterPatchDTO meter) {
        if (meterRepository.existsById(id)) {
            Meter savedMeter = meterRepository.findById(id).get();
            return meterRepository.save(meterMapper.updateMeter(savedMeter, meter));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Meter> getAll() {
        return meterRepository.findAll();
    }

    public void delete(Long id) {
        meterRepository.deleteById(id);
    }

    public Optional<Meter> findById(Long id) {
        return meterRepository.findById(id);
    }

    public Collection<Meter> findByCompany(Long id) {
        return meterRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Meter meter) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(meter.getCompany().getId());
    }

    public boolean canCreate(User user, Meter meterReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(meterReq.getCompany().getId());
        Optional<Asset> optionalAsset = assetService.findById(meterReq.getAsset().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);
        boolean second = optionalAsset.isPresent() && optionalAsset.get().getCompany().getId().equals(companyId);

        return first && second && canPatch(user, meterMapper.toDto(meterReq));
    }

    public boolean canPatch(User user, MeterPatchDTO meterReq) {
        Long companyId = user.getCompany().getId();

        Optional<MeterCategory> optionalMeterCategory = meterReq.getMeterCategory() == null ? Optional.empty() : meterCategoryService.findById(meterReq.getMeterCategory().getId());
        Optional<Image> optionalImage = meterReq.getImage() == null ? Optional.empty() : imageService.findById(meterReq.getImage().getId());
        Optional<Location> optionalLocation = meterReq.getLocation() == null ? Optional.empty() : locationService.findById(meterReq.getLocation().getId());

        //optional fields
        boolean second = meterReq.getMeterCategory() == null || (optionalMeterCategory.isPresent() && optionalMeterCategory.get().getCompanySettings().getCompany().getId().equals(companyId));
        boolean third = meterReq.getImage() == null || (optionalImage.isPresent() && optionalImage.get().getCompany().getId().equals(companyId));
        boolean fourth = meterReq.getLocation() == null || (optionalLocation.isPresent() && optionalLocation.get().getCompany().getId().equals(companyId));

        return second && third && fourth;
    }
}
