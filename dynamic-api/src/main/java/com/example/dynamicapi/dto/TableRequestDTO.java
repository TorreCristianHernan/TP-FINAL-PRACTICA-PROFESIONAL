package com.example.dynamicapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableRequestDTO {
    private String db;
    private String tableName;
    private Integer page;
    private Integer size;
    private String orderBy;
    private String orderDirection;
    private String filterColumn;
    private String filterValue;
}
