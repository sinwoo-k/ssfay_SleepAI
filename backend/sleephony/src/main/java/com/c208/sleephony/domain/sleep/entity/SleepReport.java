package com.c208.sleephony.domain.sleep.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepReport {

    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "sleep_report_tbl_gen"
    )
    @TableGenerator(
            name = "sleep_report_tbl_gen",
            table = "id_generator",
            pkColumnName = "generator_name",
            valueColumnName = "generator_value",
            pkColumnValue = "sleep_report_id",
            allocationSize = 20
    )
    @Column(name = "sleep_report_id")
    private Integer sleepReportId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "sleep_score")
    private Integer sleepScore;

    @Column(name = "sleep_time")
    private LocalDateTime sleepTime;

    @Column(name = "real_sleep_time")
    private LocalDateTime realSleepTime;

    @Column(name = "sleep_wake_time")
    private LocalDateTime sleepWakeTime;

    @Column(name = "awake_time")
    private Integer awakeTime;

    @Column(name = "rem_time")
    private Integer remTime;

    @Column(name = "nrem_time")
    private Integer nremTime;

    @Column(name = "deep_time")
    private Integer deepTime;

    @Column(name = "sleep_cycle")
    private Integer sleepCycle;

    @CreationTimestamp               // ← 추가
    @Column(name = "created_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
