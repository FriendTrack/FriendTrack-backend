package com.ciklon.friendtracker.api.dto.rating;

import com.ciklon.friendtracker.api.dto.PaginationResponse;

import java.util.List;

public class RatingPaginationResponse extends PaginationResponse<RatingDto> {

    public RatingPaginationResponse(int page, int size, int totalPages, List<RatingDto> content) {
        super(size, page, totalPages, content);
    }
}
