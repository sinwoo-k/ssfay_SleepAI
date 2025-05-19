package com.c208.sleephony.domain.sleep.utils;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SleepStageFilter {

    private static final int K = 5;
    private static final int MIN_CONSEC = 2;

    private final Deque<SleepStage> recent = new ArrayDeque<>();
    private SleepStage prev = SleepStage.AWAKE;

    private static final Map<SleepStage, Set<SleepStage>> ALLOWED = Map.of(
            SleepStage.AWAKE, Set.of(SleepStage.AWAKE, SleepStage.NREM1),
            SleepStage.NREM1, Set.of(SleepStage.AWAKE, SleepStage.NREM1, SleepStage.NREM2),
            SleepStage.NREM2, Set.of(SleepStage.AWAKE, SleepStage.NREM2, SleepStage.NREM3),
            SleepStage.NREM3, Set.of(SleepStage.AWAKE, SleepStage.NREM3, SleepStage.NREM2),
            SleepStage.REM, Set.of(SleepStage.AWAKE, SleepStage.NREM1, SleepStage.REM)
    );

    public synchronized SleepStage filter(List<SleepStage> latestFive) {
        latestFive.forEach(this::push);
        SleepStage candidate = mode();

        if(!ALLOWED.get(prev).contains(candidate))
            return prev;
        if(consecutiveCount(candidate) < MIN_CONSEC)
            return prev;
        prev = candidate;
        return prev;
    }

    private void push(SleepStage sleepStage) {
        if(recent.size() == K) recent.removeFirst();
        recent.addLast(sleepStage);
    }

    private SleepStage mode() {
        return recent.stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.<SleepStage,Long>comparingByValue()
                        .thenComparing((e -> lastIndex(e.getKey()))))
                .map(Map.Entry::getKey)
                .orElse(prev);
    }
    private long lastIndex(SleepStage sleepStage) {
        int idx = 0;
        for(SleepStage s : recent) {
            if(s == sleepStage) idx++;
        }
        return idx;
    }
    private int consecutiveCount(SleepStage sleepStage) {
        int count = 0;
        Iterator<SleepStage> iter = recent.descendingIterator();
        while(iter.hasNext() && iter.next() == sleepStage) count++;
        return count;
    }
}
