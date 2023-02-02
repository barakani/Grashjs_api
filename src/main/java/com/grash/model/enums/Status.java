package com.grash.model.enums;

import java.util.Arrays;
import java.util.List;

public enum Status {
    OPEN(Arrays.asList("Open", "Ouvert")),
    IN_PROGRESS(Arrays.asList("In Progress", "En cours")),
    ON_HOLD(Arrays.asList("On hold", "En pause")),
    COMPLETE(Arrays.asList("Complete", "Terminé"));
    private final List<String> strings;

    Status(List<String> strings) {
        this.strings = strings;
    }

    public static Status getStatusFromString(String string) {
        for (Status status : Status.values()) {
            if (status.strings.stream().anyMatch(str -> str.equalsIgnoreCase(string))) {
                return status;
            }
        }
        return OPEN;
    }

    public String getName() {
        switch (this) {
            case OPEN:
                return "Open";
            case IN_PROGRESS:
                return "In Progress";
            case ON_HOLD:
                return "On Hold";
            case COMPLETE:
                return "Complete";
            default:
                return this.toString();
        }
    }
}
