package com.grash.advencedsearch.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfo {
    private int pageNumber;
    private int pageSize;
    private int totalNumberOfPages;
    private long totalNumberOfRecords;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private long totalDb;
}
