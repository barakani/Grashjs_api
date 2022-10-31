package com.grash.advancedsearch;


import com.grash.advancedsearch.pagination.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private FilterField[] filterFields;
    private Boolean advancedSearch;
    private String searchTerm;
    private String sortedBy;
    private Order order;
}
