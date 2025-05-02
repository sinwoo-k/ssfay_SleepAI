package com.c208.sleephony.domain.sleep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleepLevelId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "level", length = 5)
    private String level; // AWAKE, REM, NREM1, NREM2, NREM3

    @Column(name = "score")
    private Integer score;

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;

    @Column(name = "created_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
