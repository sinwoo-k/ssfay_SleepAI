package com.c208.sleephony.domain.sleep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleepSessionId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
