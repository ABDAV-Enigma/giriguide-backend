package com.abdav.giri_guide.mapper;

import java.util.List;

import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;

public class TourGuideMapper {
    private TourGuideMapper() {
    }

    public static TourGuideDetailResponse toTourGuideDetailResponse(
            TourGuide tourGuide,
            List<TourGuideHikingPoint> tourGuideHikingPoint

    ) {
        return new TourGuideDetailResponse(
                tourGuide.getId(),
                tourGuide.getName(),
                tourGuide.getImage().getPath(),
                tourGuide.getDescription(),
                tourGuide.isActive(),
                // TODO fix this hard code
                0.0,
                200,
                10,
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                TourGuideHikingPointMapper.toListMountainListHikingPoint(tourGuideHikingPoint)

        );
    }
}