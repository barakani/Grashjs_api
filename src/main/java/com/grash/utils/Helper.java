package com.grash.utils;


import com.grash.model.OwnUser;
import com.grash.model.enums.Language;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date addSeconds(Date date, int seconds) {
        return new Date(date.getTime() + seconds * 1000);
    }

    public static Locale getLocale(OwnUser user) {
        Language language = user.getCompany().getCompanySettings().getGeneralPreferences().getLanguage();
        switch (language) {
            case FR:
                return Locale.FRANCE;
            default:
                return Locale.getDefault();
        }
    }

    public static Date getDateFromJsString(String string) {
        DateFormat jsfmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return jsfmt.parse(string);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Date getDateFromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        try {
            return DateFormat.getDateInstance().parse(string);
        } catch (Exception exception) {
            return null;
        }
    }

    public static String getEmails(Collection<OwnUser> users) {
        return users.stream().map(OwnUser::getEmail).reduce("", (acc, email) -> acc + ", " + email);
    }

    public static boolean getBooleanFromString(String string) {
        List<String> trues = Arrays.asList("true", "Yes", "Oui");
        return trues.stream().anyMatch(value -> value.equalsIgnoreCase(string));
    }

    public static String getStringFromBoolean(boolean bool, MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(bool ? "Yes" : "No", null, locale);
    }
}
