package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.User;

public interface TourGuideRepository extends JpaRepository<TourGuide, String> {
    Optional<TourGuide> findByNikAndDeletedDateIsNull(String nik);

    Optional<TourGuide> findByIdAndDeletedDateIsNull(String id);

    Optional<TourGuide> findByUsersAndDeletedDateIsNull(User users);
}
