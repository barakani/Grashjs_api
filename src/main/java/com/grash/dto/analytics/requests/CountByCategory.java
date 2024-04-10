package com.grash.dto.analytics.workOrders;

import com.grash.dto.CategoryMiniDTO;
import com.grash.dto.UserMiniDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountByCategory extends CategoryMiniDTO {
    private int count;
}
