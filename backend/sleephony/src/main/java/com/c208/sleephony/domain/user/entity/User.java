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

    @Builder.Default
    @Column(name = "theme_id", nullable = false)
    private Integer themeId=1;

    @Builder.Default
    @Column(name = "email", nullable = false, length = 50)
    private String email = "";

    @Builder.Default
    @Column(name = "nickname", length = 30)
    private String nickname = "nickname";

    @Builder.Default
    @Column(name = "height")
    private Float height=0f;

    @Builder.Default
    @Column(name = "weight")
    private Float weight=0f;

    @Builder.Default
    @Column(name = "birth_date")
    private LocalDate birthDate = LocalDate.of(1900, 1, 1);

    @Builder.Default
    @Column(name = "gender", length = 1)
    private String gender="M";

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @Builder.Default
    @Column(name = "deleted", nullable = false, length = 1)
    private Character deleted='N';

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "social", nullable = false, length = 6)
    private Social social = Social.GOOGLE;

}
