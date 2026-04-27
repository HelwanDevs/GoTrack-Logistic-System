package com.gotrack.auth_service.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ListAccountsResponse {

    private List<AccountResponse> accounts;

    private long totalCount;

    private int page;

    private int size;

    private int totalPages;

    public ListAccountsResponse(List<AccountResponse> accounts, long totalCount, int page, int size) {
        this.accounts = accounts;
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalCount / size);
    }

}
