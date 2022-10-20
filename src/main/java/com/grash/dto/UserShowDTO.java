package com.grash.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserShowDTO {
    private Long id;
    private String firstName;
    private String lastName;
}