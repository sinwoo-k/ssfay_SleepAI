package com.c208.sleephony.domain.theme.entity;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sound")
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sound_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sound_theme_id", nullable = false)
    private Theme theme;

    @Column(name = "sound_name", length = 50, nullable = false)
    private String name;

    @Column(name = "sound_url", length = 512, nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "sleep_stage", length = 5)
    private SleepStage sleepStage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
