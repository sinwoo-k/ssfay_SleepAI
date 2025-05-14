package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RawSequenceResponse {
    private List<String> labels;
}
