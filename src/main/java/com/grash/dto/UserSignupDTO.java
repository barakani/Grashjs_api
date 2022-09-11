package com.grash.dto;

import com.grash.model.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserSignupDTO {

    @ApiModelProperty(position = 1)
    @NotNull
    private String email;
    @ApiModelProperty(position = 2)
    @NotNull
    private String password;
    @NotNull
    @ApiModelProperty(position = 3)
    private Role role;
}
