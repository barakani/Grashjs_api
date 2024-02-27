package com.grash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    private String type;
    private WorkOrderBaseShowDTO event;
    private Date date;
}
