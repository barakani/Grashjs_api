package com.grash.advancedsearch;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private List<FilterField> filterFields;
    private Direction direction = Direction.ASC;
    private int pageNum = 0;
    private int pageSize = 10;
}
