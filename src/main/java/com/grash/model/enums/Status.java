package com.grash.model.enums;

public enum Status {
    OPEN,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETE;

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
