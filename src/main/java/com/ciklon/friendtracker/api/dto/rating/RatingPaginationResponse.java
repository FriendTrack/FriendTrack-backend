package com.ciklon.friendtracker.api.dto.rating;

import com.ciklon.friendtracker.api.dto.PaginationResponse;

import java.util.List;

public class RatingPaginationResponse extends PaginationResponse<RatingDto> {

    public RatingPaginationResponse(int size, int page, int totalPages, List<RatingDto> content) {
        super(size, page, totalPages, content);
    }
}
