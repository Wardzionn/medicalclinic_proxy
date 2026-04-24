package com.wardzionn.githubproxy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageDto<T> {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private List<T> content;

}
