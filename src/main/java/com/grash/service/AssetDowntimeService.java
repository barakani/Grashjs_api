package com.grash.service;

import com.grash.dto.AssetDowntimePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.AssetDowntimeMapper;
import com.grash.model.AssetDowntime;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.AssetDowntimeRepository;
import com.grash.utils.DowntimeComparator;
import com.grash.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AssetDowntimeService {
    private final AssetDowntimeRepository assetDowntimeRepository;
    private final CompanyService companyService;
    private final AssetDowntimeMapper assetDowntimeMapper;

    public AssetDowntime create(AssetDowntime assetDowntime) {
        return assetDowntimeRepository.save(assetDowntime);
    }

    public AssetDowntime save(AssetDowntime assetDowntime) {
        return assetDowntimeRepository.save(assetDowntime);
    }

    public AssetDowntime update(Long id, AssetDowntimePatchDTO assetDowntime) {
        if (assetDowntimeRepository.existsById(id)) {
            AssetDowntime savedAssetDowntime = assetDowntimeRepository.findById(id).get();
            return assetDowntimeRepository.save(assetDowntimeMapper.updateAssetDowntime(savedAssetDowntime, assetDowntime));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<AssetDowntime> getAll() {
        return assetDowntimeRepository.findAll();
    }

    public void delete(Long id) {
        assetDowntimeRepository.deleteById(id);
    }

    public Optional<AssetDowntime> findById(Long id) {
        return assetDowntimeRepository.findById(id);
    }

    public Collection<AssetDowntime> findByAsset(Long id) {
        return assetDowntimeRepository.findByAsset_Id(id);
    }

    public Collection<AssetDowntime> findByCompany(Long id) {
        return assetDowntimeRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(OwnUser user, AssetDowntime assetDowntime) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(assetDowntime.getAsset().getCompany().getId());
    }

    public boolean canCreate(OwnUser user, AssetDowntime assetDowntimeReq) {
        return true;
    }

    public boolean canPatch(OwnUser user, AssetDowntimePatchDTO assetDowntimeReq) {
        return true;
    }

    public Collection<AssetDowntime> findByStartsOnBetweenAndCompany(Date date1, Date date2, Long id) {
        return assetDowntimeRepository.findByStartsOnBetweenAndCompany_Id(date1, date2, id);

    }

    public long getDowntimesMeantime(Collection<AssetDowntime> downtimes) {
        long result = 0;
        if (downtimes.size() > 2) {
            DowntimeComparator downtimeComparator = new DowntimeComparator();
            AssetDowntime firstDowntime = Collections.max(downtimes, downtimeComparator);
            AssetDowntime lastDowntime = Collections.min(downtimes, downtimeComparator);
            result = (Helper.getDateDiff(firstDowntime.getStartsOn(), lastDowntime.getStartsOn(), TimeUnit.HOURS)) / (downtimes.size() - 1);
        }
        return result;
    }
}
