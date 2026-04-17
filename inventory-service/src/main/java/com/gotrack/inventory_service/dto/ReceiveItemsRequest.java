package com.gotrack.inventory_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReceiveItemsRequest {
    private Long productId;
    private Long branchId;
    private Long pickupRequestId;
    private List<String> uniqueSkus;
}