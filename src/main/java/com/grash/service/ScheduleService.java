package com.grash.service;

import com.grash.dto.SchedulePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.ScheduleMapper;
import com.grash.model.PreventiveMaintenance;
import com.grash.model.Schedule;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
import com.grash.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PreventiveMaintenanceService preventiveMaintenanceService;
    private final ScheduleMapper scheduleMapper;

    public Schedule create(Schedule Schedule) {
        return scheduleRepository.save(Schedule);
    }

    public Schedule update(Long id, SchedulePatchDTO schedule) {
        if (scheduleRepository.existsById(id)) {
            Schedule savedSchedule = scheduleRepository.findById(id).get();
            return scheduleRepository.save(scheduleMapper.updateSchedule(savedSchedule, schedule));
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Collection<Schedule> findByCompany(Long id) {
        return scheduleRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Schedule schedule) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(schedule.getPreventiveMaintenance().getCompany().getId());
    }

    public boolean canCreate(User user, Schedule scheduleReq) {

        return canPatch(user, scheduleMapper.toDto(scheduleReq));
    }

    public boolean canPatch(User user, SchedulePatchDTO scheduleReq) {
        Long companyId = user.getCompany().getId();

        Optional<PreventiveMaintenance> optionalPreventiveMaintenance = scheduleReq.getPreventiveMaintenance() == null ? Optional.empty() : preventiveMaintenanceService.findById(scheduleReq.getPreventiveMaintenance().getId());

        boolean first = scheduleReq.getPreventiveMaintenance() == null || (optionalPreventiveMaintenance.isPresent() && optionalPreventiveMaintenance.get().getCompany().getId().equals(companyId));

        return first;
    }


}
