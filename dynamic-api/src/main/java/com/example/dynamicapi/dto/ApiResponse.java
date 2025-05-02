package com.example.dynamicapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    // Getters y setters
    private boolean success;
    private String message;
    private T data;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;

    public ApiResponse() {}

    private ApiResponse(boolean success, String message, T data,
                        Long totalElements, Integer totalPages, Integer currentPage) {
        this.success       = success;
        this.message       = message;
        this.data          = data;
        this.totalElements = totalElements;
        this.totalPages    = totalPages;
        this.currentPage   = currentPage;
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(true, msg, data, null, null, null);
    }

    public static <T> ApiResponse<T> success(String msg,
                                             T data,
                                             Long totalElements,
                                             Integer totalPages,
                                             Integer currentPage) {
        return new ApiResponse<>(true, msg, data, totalElements, totalPages, currentPage);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(false, msg, null, null, null, null);
    }

}
