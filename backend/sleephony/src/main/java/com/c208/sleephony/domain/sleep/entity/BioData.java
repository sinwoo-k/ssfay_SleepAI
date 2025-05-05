package com.c208.sleephony.domain.sleep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bio_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BioData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bio_data_id")
    private Long bioDataId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "heart_rate")
    private Byte heartRate;

    @Column(name = "gyro_x")
    private Float gyroX;

    @Column(name = "gyro_y")
    private Float gyroY;

    @Column(name = "gyro_z")
    private Float gyroZ;

    @Column(name = "body_temperature")
    private Float bodyTemperature;

    @Column(name = "created_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;
}
