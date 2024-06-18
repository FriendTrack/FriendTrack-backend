package com.ciklon.friendtracker.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponse<T> {
    @Schema(description = "Номер страницы", example = "1")
    int page;

    @Schema(description = "Размер страницы", example = "10")
    int size;

    @Schema(description = "Общее количество страниц", example = "10")
    int totalPages;

    @Schema(description = "Список рейтингов")
    private List<T> content;
}
