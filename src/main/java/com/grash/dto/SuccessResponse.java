package com.grash.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse {
    private boolean success;
    private String message;

    public SuccessResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
