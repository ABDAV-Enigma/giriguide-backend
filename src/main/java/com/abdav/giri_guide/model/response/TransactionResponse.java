package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String transactionStatus,
        String hikingPointId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String customerId,
        String guideId,
        Integer porter,
        Integer hikerQty,
        Double pricePorter,
        Double totalTourGuidePrice,
        Double totalAdditionalPrice,
        Double totalSimaksiPrice,
        Double totalEntryPrice,
        Double adminCost,
        Double totalPrice
) {
}