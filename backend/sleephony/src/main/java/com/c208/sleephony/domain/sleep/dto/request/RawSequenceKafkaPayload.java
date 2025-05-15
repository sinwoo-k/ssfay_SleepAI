package com.c208.sleephony.domain.sleep.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RawSequenceKafkaPayload(
        List<Float> accX, List<Float> accY, List<Float> accZ,
        List<Float> temp, List<Float> hr) {}