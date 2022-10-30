package com.grash.advencedsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterField {
    private String field;
    private Object value;
    private String operation;
}
