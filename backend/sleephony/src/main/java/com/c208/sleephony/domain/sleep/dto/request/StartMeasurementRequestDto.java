package com.c208.sleephony.domain.sleep.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "측정 시작 요청")
public class StartMeasurementRequestDto {

    @Schema(description = "측정 시작 시각", example = "2025-04-28T22:30:00")
    @JsonProperty("started_at")
    private LocalDateTime startedAt;
}
