package com.ciklon.friendtracker.api.dto.rating;

import com.ciklon.friendtracker.api.dto.PaginationResponse;

import java.util.List;

public class AverageRatingPaginationResponse extends PaginationResponse<AverageRatingDto> {

    public AverageRatingPaginationResponse(int page, int size, int totalPages, List<AverageRatingDto> content) {
        super(size, page, totalPages, content);
    }
}
