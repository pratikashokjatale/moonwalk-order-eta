package com.moonwalk.ordereta.algorithm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EtaResult {
    private Long estimatedTimeSeconds;
}
