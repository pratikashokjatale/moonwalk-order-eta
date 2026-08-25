package com.moonwalk.ordereta.entity;

import com.moonwalk.ordereta.enums.EtaStrategyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "eta_executions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtaExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long estimatedTimeSeconds;

    private Long elapsedTimeSeconds;
    private Long remainingTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtaStrategyType algorithmUsed;

    @Column(nullable = false)
    private int pendingOrderCount;

    @Column(nullable = false)
    private int availableChefCount;

    @Column(nullable = false)
    private int busyStationCount;

    @Column(nullable = false)
    private String status; // E.g., "CALCULATED", "RECALCULATED"
}
