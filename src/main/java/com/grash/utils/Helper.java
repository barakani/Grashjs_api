package com.grash.utils;


import com.grash.model.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Helper {

    public String generateString() {
        return UUID.randomUUID().toString();
    }

    public HttpHeaders getPagingHeaders(Page<?> page, int size, String name) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Range", name + (page.getNumber() - 1) * size + "-" + page.getNumber() * size + "/" + page.getTotalElements());
        responseHeaders.set("Access-Control-Expose-Headers", "Content-Range");
        return responseHeaders;
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static Date incrementDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date getNextOccurence(Date date, int days) {
        Date result = date;
        if (result.after(new Date())) {
            result = incrementDays(result, days);
        } else
            while (result.before(new Date())) {
                result = incrementDays(result, days);
            }
        return result;
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static long getAverageAge(Collection<WorkOrder> completeWorkOrders) {
        List<Long> completionTimes = completeWorkOrders.stream().map(workOrder -> Helper.getDateDiff(Date.from(workOrder.getCreatedAt()), workOrder.getCompletedOn(), TimeUnit.DAYS)).collect(Collectors.toList());
        return completionTimes.size() == 0 ? 0 : completionTimes.stream().mapToLong(value -> value).sum() / completionTimes.size();
    }
}
