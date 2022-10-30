package com.grash.advencedsearch.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataTablePagination implements Serializable {
    private int page;
    private String sortField;
    private SortDirection sortDirection;
    private int pageSize;

    @JsonIgnore
    public static DataTablePagination paginationStreamPagination() {
        return DataTablePagination.builder().page(0).pageSize(50).sortDirection(SortDirection.ASC)
                .sortField("id").build();
    }

}
