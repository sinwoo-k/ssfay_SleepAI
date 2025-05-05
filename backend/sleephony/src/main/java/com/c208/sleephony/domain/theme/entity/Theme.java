package com.c208.sleephony.domain.theme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Integer id;

    @Column(name = "theme_name", length = 20, nullable = false)
    private String name;

    @Column(name = "theme_description", length = 512, nullable = false)
    private String description;

    @Column(name = "image_url", length = 215, nullable = false)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
