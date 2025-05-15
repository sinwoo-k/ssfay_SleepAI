package com.c208.sleephony.domain.sleep.dto.request;

import com.c208.sleephony.global.utils.EqualLengthLists;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualLengthLists
public class RawSequenceRequest {
    private List<Float> accX;
    private List<Float> accY;
    private List<Float> accZ;
    private List<Float> temp;
    private List<Float> hr;

    @NotNull(message = "measured_at(측정 시각)을 반드시 넘겨주세요")
    private LocalDateTime measuredAt;

}
