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
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "bio_data_tbl_gen"
    )
    @TableGenerator(
            name = "bio_data_tbl_gen",
            table = "id_generator",
            pkColumnName = "generator_name",
            valueColumnName = "generator_value",
            pkColumnValue = "bio_data_id",
            allocationSize = 20
    )    @Column(name = "sleep_level_id")
    private Long sleepLevelId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "level", length = 5)
    private String level; // AWAKE, REM, NREM1, NREM2, NREM3

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;

    @Column(name = "created_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
