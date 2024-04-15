package com.grash.service;

import com.grash.dto.SchedulePatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.ScheduleMapper;
import com.grash.model.*;
import com.grash.repository.ScheduleRepository;
import com.grash.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PreventiveMaintenanceService preventiveMaintenanceService;
    private final ScheduleMapper scheduleMapper;
    private final MessageSource messageSource;
    private final EmailService2 emailService2;
    private final WorkOrderService workOrderService;
    private final TaskService taskService;
    @Value("${frontend.url}")
    private String frontendUrl;
    private Map<Long, List<Timer>> timersState = new HashMap<>();

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

    public void scheduleWorkOrder(Schedule schedule) {
        boolean shouldSchedule = !schedule.isDisabled() && (schedule.getEndsOn() == null || schedule.getEndsOn().after(new Date()));
        if (shouldSchedule) {
            Timer timer = new Timer();
            //  Collection<WorkOrder> workOrders = workOrderService.findByPM(schedule.getPreventiveMaintenance().getId());
            Date startsOn = Helper.getNextOccurence(schedule.getStartsOn(), schedule.getFrequency());
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {//create WO
                    PreventiveMaintenance preventiveMaintenance = schedule.getPreventiveMaintenance();
                    WorkOrder workOrder = workOrderService.getWorkOrderFromWorkOrderBase(preventiveMaintenance);
                    Collection<Task> tasks = taskService.findByPreventiveMaintenance(preventiveMaintenance.getId());
                    workOrder.setParentPreventiveMaintenance(schedule.getPreventiveMaintenance());
                    if (schedule.getDueDateDelay() != null) {
                        workOrder.setDueDate(Helper.incrementDays(new Date(), schedule.getDueDateDelay()));
                    }
                    WorkOrder savedWorkOrder = workOrderService.create(workOrder);
                    tasks.forEach(task -> {
                        Task copiedTask = new Task(task.getTaskBase(), savedWorkOrder, null, task.getValue());
                        taskService.create(copiedTask);
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, startsOn, (long) schedule.getFrequency() * 24 * 60 * 60 * 1000);
            List<Timer> localTimers = new ArrayList<>();
            localTimers.add(timer);//first wo creation

            Timer timer1 = new Timer();// use daysBeforePrevMaintNotification
            int daysBeforePMNotification = schedule.getPreventiveMaintenance().getCompany()
                    .getCompanySettings().getGeneralPreferences().getDaysBeforePrevMaintNotification();
            TimerTask timerTask1 = new TimerTask() {
                @Override
                public void run() {
                    //send notification to assigned users
                    PreventiveMaintenance preventiveMaintenance = schedule.getPreventiveMaintenance();
                    Locale locale = Helper.getLocale(preventiveMaintenance.getCompany());
                    String title = messageSource.getMessage("coming_wo", null, locale);
                    Collection<OwnUser> usersToMail = preventiveMaintenance.getUsers();
                    Map<String, Object> mailVariables = new HashMap<String, Object>() {{
                        put("pmLink", frontendUrl + "/app/preventive-maintenances/" + preventiveMaintenance.getId());
                        put("featuresLink", frontendUrl + "/#key-features");
                        put("pmTitle", preventiveMaintenance.getTitle());
                    }};
                    emailService2.sendMessageUsingThymeleafTemplate(usersToMail.stream().map(OwnUser::getEmail)
                            .toArray(String[]::new), title, mailVariables, "coming-work-order.html", locale);
                }
            };
            timer1.scheduleAtFixedRate(timerTask1, Helper.minusDays(startsOn, daysBeforePMNotification), (long) schedule.getFrequency() * 24 * 60 * 60 * 1000);
            localTimers.add(timer1);

            if (schedule.getEndsOn() != null) {
                Timer timer2 = new Timer();
                TimerTask timerTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        //stop other timers
                        timersState.get(schedule.getId()).get(0).cancel();
                        timersState.get(schedule.getId()).get(0).purge();
                        timersState.get(schedule.getId()).get(1).cancel();
                        timersState.get(schedule.getId()).get(1).purge();
                    }
                };
                timer2.schedule(timerTask2, schedule.getEndsOn());
                localTimers.add(timer2); //third schedule stopping
            }
            timersState.put(schedule.getId(), localTimers);
        }
    }

    public void reScheduleWorkOrder(Long id, Schedule schedule) {
        stopScheduleTimers(id);
        scheduleWorkOrder(schedule);
    }

    public void stopScheduleTimers(Long id) {
        timersState.get(id).forEach(timer -> {
            timer.cancel();
            timer.purge();
        });
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.saveAndFlush(schedule);
    }
}
