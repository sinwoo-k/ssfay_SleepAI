package com.c208.sleephony.domain.sleep.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RawSequenceRequest {
    private List<Float> accX;
    private List<Float> accY;
    private List<Float> accZ;
    private List<Float> temp;
    private List<Float> hr;
}
