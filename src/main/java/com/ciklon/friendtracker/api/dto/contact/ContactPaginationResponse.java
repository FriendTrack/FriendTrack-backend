package com.ciklon.friendtracker.api.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ContactPaginationResponse(
        @Schema(description = "Список контактов")
        List<ContactDto> contactDtoList,

        @Schema(description = "Размер страницы")
        Integer size,

        @Schema(description = "Номер страницы")
        Integer page,

        @Schema(description = "Общее количество элементов")
        Integer totalPages
) {
}
