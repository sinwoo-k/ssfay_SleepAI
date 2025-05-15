package com.c208.sleephony.domain.sleep.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Builder                // ← 빌더 패턴
@NoArgsConstructor      // 직렬화를 위해 기본생성자 포함
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RawSequenceResponse {

    private String        requestId;

    /** List 빌더에 one‑by‑one 추가하고 싶으면 @Singular */
    @Singular
    private List<String>  labels;
}