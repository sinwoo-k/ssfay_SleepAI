package com.c208.sleephony.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "theme_id", nullable = false)
    private Integer themeId;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "nickname", length = 30)
    private String nickname;

    @Column(name = "height")
    private Float height;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false, length = 1)
    private Character deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "social", nullable = false, length = 6)
    private Social social;

}
