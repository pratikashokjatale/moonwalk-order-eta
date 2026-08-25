package com.moonwalk.ordereta.entity;

import com.moonwalk.ordereta.enums.StationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cooking_stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // e.g., OVEN, GRILL, DRINKS

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StationStatus status = StationStatus.AVAILABLE;

    @Column(nullable = false)
    private Integer currentWorkload = 0;

    @PrePersist
    public void prePersist() {
        if (this.currentWorkload == null) this.currentWorkload = 0;
        if (this.status == null) this.status = StationStatus.AVAILABLE;
    }
}
