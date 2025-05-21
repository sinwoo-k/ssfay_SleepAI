package com.c208.sleephony.domain.sleep.utils;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 후보정(Post-correction) 알고리즘: 모드 기반으로 추정된 단계에 대해,
 * 생리학적 비연속(2단계 이상 점프)만 보정하며,
 * 그 외엔 원 후보를 그대로 반영합니다.
 */
@RequiredArgsConstructor
public class SleepStageFilter {
    private static final int K = 5;
    private final Deque<SleepStage> recent = new ArrayDeque<>();
    private SleepStage prev = SleepStage.AWAKE;

    // 단계 순서 정의 (인덱스로 거리 계산)
    private static final List<SleepStage> SLEEP_ORDER = List.of(
            SleepStage.AWAKE,
            SleepStage.NREM1,
            SleepStage.NREM2,
            SleepStage.NREM3,
            SleepStage.REM
    );

    /**
     * 후처리 방식(post-correction):
     * 1) mode로 추정된 candidate를 얻음
     * 2) candidate와 prev 간 거리가 2 이상이면 prev+1 단계로 보정
     * 3) 나머지는 candidate 그대로 반영
     */
    public synchronized SleepStage filter(List<SleepStage> latestFive) {
        // sliding window
        latestFive.forEach(this::push);
        // 최빈값(primary candidate)
        SleepStage candidate = mode();

        int prevIdx = SLEEP_ORDER.indexOf(prev);
        int candIdx = SLEEP_ORDER.indexOf(candidate);
        int gap = candIdx - prevIdx;

        // 2단계 이상 점프만 보정
        if (gap >= 2) {
            // 가능한 다음 단계로만 이동
            if (prevIdx < SLEEP_ORDER.size() - 1) {
                prev = SLEEP_ORDER.get(prevIdx + 1);
            }
        } else if (gap <= -2) {
            // 역방향 2단계 이상 점프(예: REM->NREM2)도 한 단계 얕게 보정
            if (prevIdx > 0) {
                prev = SLEEP_ORDER.get(prevIdx - 1);
            }
        } else {
            // gap -1,0,1: candidate 그대로 반영
            prev = candidate;
        }
        return prev;
    }

    private void push(SleepStage stage) {
        if (recent.size() == K) recent.removeFirst();
        recent.addLast(stage);
    }

    private SleepStage mode() {
        return recent.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.<SleepStage, Long>comparingByValue()
                        .thenComparing(e -> lastIndex(e.getKey())))
                .map(Map.Entry::getKey)
                .orElse(prev);
    }

    private long lastIndex(SleepStage stage) {
        return recent.stream().filter(s -> s == stage).count();
    }
}
