package com.moonwalk.ordereta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountdownResponse {
    private Long orderId;
    private Long estimatedDurationSeconds;
    private Long elapsedSeconds;
    private Long remainingSeconds;
}
