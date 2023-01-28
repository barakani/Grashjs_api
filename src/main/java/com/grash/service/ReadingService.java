package com.grash.service;

import com.grash.dto.ReadingPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.ReadingMapper;
import com.grash.model.OwnUser;
import com.grash.model.Reading;
import com.grash.model.enums.RoleType;
import com.grash.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingService {
    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;
    private final MeterService meterService;

    public Reading create(Reading Reading) {
        return readingRepository.save(Reading);
    }

    public Reading update(Long id, ReadingPatchDTO reading) {
        if (readingRepository.existsById(id)) {
            Reading savedReading = readingRepository.findById(id).get();
            return readingRepository.save(readingMapper.updateReading(savedReading, reading));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Reading> getAll() {
        return readingRepository.findAll();
    }

    public void delete(Long id) {
        readingRepository.deleteById(id);
    }

    public Optional<Reading> findById(Long id) {
        return readingRepository.findById(id);
    }

    public Collection<Reading> findByCompany(Long id) {
        return readingRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(OwnUser user, Reading reading) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(reading.getMeter().getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Reading readingReq) {
        Long companyId = user.getCompany().getId();
        //@NotNull fields
        boolean first = meterService.isMeterInCompany(readingReq.getMeter(), companyId, false);
        return first && canPatch(user, readingMapper.toPatchDto(readingReq));
    }

    public boolean canPatch(OwnUser user, ReadingPatchDTO readingReq) {
        Long companyId = user.getCompany().getId();
        return meterService.isMeterInCompany(readingReq.getMeter(), companyId, true);
    }

    public Collection<Reading> findByMeter(Long id) {
        return readingRepository.findByMeter_Id(id);
    }
}
