package com.c208.sleephony.domain.sleep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "sleep_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleep_statistics_id")
    private Integer sleepStatisticId;

    @Column(name = "age_group")
    private String ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "bedtime")
    private LocalTime bedtime;
    @Column(name = "wakeup_time")
    private LocalTime wakeupTime;

    @Column(name = "sleep_duration_minutes")
    private int sleepDurationMinutes;
    @Column(name = "sleep_latency_minutes")
    private int sleepLatencyMinutes;
    @Column(name = "rem_sleep_minutes")
    private int remSleepMinutes;
    @Column(name = "rem_sleep_ratio")
    private int remSleepRatio;
    @Column(name = "light_sleep_minutes")
    private int lightSleepMinutes;
    @Column(name = "light_sleep_ratio")
    private int lightSleepRatio;
    @Column(name = "deep_sleep_minutes")
    private int deepSleepMinutes;
    @Column(name = "deep_sleep_ratio")
    private int deepSleepRatio;

    public enum Gender {
        M,F
    }
}
