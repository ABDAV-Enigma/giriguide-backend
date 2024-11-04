package com.abdav.giri_guide.entity;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "m_tour_guide")
@EqualsAndHashCode(callSuper = true)
@Entity
public class TourGuide extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User users;
    private String name;
    private String nik;
    private LocalDate birthDate;
    private String description;

    @Nullable
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;

    @Builder.Default
    private Integer maxHiker = 5;
    private Double price;
    private Double additionalPrice;
    private Integer totalPorter;
    private Double pricePorter;

    @Builder.Default
    private boolean isActive = false;

}