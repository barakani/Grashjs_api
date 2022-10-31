package com.grash.advancedsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterField {
    private String field;
    private Object value;
    private String operation;
    private List<Long> values = new ArrayList<>();
}
