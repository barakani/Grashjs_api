package com.grash.service;

import com.grash.dto.CurrencyPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.CurrencyMapper;
import com.grash.model.Currency;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final AssetService assetService;
    private final LocationService locationService;
    private final MeterService meterService;
    private final PartService partService;
    private final TeamService teamService;
    private final PreventiveMaintenanceService preventiveMaintenanceService;
    private final WorkOrderService workOrderService;
    private final CurrencyMapper currencyMapper;

    public Currency create(Currency Currency) {
        return currencyRepository.save(Currency);
    }

    public Currency update(Long id, CurrencyPatchDTO currencyPatchDTO) {
        if (currencyRepository.existsById(id)) {
            Currency currency = currencyRepository.findById(id).get();
            return currencyRepository.save(currencyMapper.updateCurrency(currency, currencyPatchDTO));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public void delete(Long id) {
        currencyRepository.deleteById(id);
    }

    public Optional<Currency> findById(Long id) {
        return currencyRepository.findById(id);
    }

    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    public boolean hasAccess(OwnUser user) {
        return user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN) ||
                user.getRole().getRoleType().equals(RoleType.ROLE_CLIENT);
    }

    public boolean canCreate(OwnUser user) {
        return canPatch(user);
    }

    public boolean canPatch(OwnUser user) {
        Long companyId = user.getCompany().getId();

        boolean first = user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN);

        boolean second = user.getAsset() == null || user.getAsset().stream().allMatch(item ->
                assetService.isAssetInCompany(item,companyId,false));
        boolean third = user.getLocation() == null || user.getLocations().stream().allMatch(item ->
                locationService.isLocationInCompany(item,companyId,false));
        boolean fourth = user.getMeters() == null || user.getMeters().stream().allMatch(item ->
                meterService.isMeterInCompany(item,companyId,false));
        boolean fifth = user.getParts() == null || user.getParts().stream().allMatch(item ->
                partService.isPartInCompany(item,companyId,false));
        boolean sixth = user.getTeams() == null || user.getTeams().stream().allMatch(item ->
                teamService.isTeamInCompany(item,companyId,false));
        boolean seventh = user.getPreventiveMaintenances() == null || user.getPreventiveMaintenances().stream().allMatch(item ->
                preventiveMaintenanceService.isPreventiveMaintenanceInCompany(item,companyId,false));
        boolean eighth = user.getWorkOrders() == null || user.getWorkOrders().stream().allMatch(item ->
                workOrderService.isWorkOrderInCompany(item,companyId,false));
        return first && second && third && fourth && fifth && sixth && seventh && eighth;
    }
}
