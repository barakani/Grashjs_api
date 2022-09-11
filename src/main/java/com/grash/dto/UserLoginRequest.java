package com.grash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String type;
}
