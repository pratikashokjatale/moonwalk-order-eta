package com.moonwalk.ordereta.entity;

import com.moonwalk.ordereta.enums.ChefStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chefs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChefStatus status = ChefStatus.AVAILABLE;

    @Column(nullable = false)
    private Integer currentWorkload = 0;

    @PrePersist
    public void prePersist() {
        if (this.currentWorkload == null) this.currentWorkload = 0;
        if (this.status == null) this.status = ChefStatus.AVAILABLE;
    }
}
