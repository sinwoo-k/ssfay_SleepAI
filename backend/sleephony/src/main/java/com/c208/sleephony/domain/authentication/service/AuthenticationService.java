package com.c208.sleephony.domain.authentication.service;

import com.c208.sleephony.domain.authentication.dto.request.GoogleLoginRequest;
import com.c208.sleephony.domain.authentication.dto.response.LoginResponse;
import com.c208.sleephony.domain.authentication.util.KakaoTokenVerifier;
import com.c208.sleephony.global.utils.JwtProvider;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final KakaoTokenVerifier kakaoTokenVerifier;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private static final String SOCIAL_GOOGLE = "GOOGLE";
    private static final String SOCIAL_KAKAO = "KAKAO";

    public LoginResponse loginWithGoogle(GoogleLoginRequest request) {

        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailAndSocialAndDeleted(email, SOCIAL_GOOGLE, 'N');

        User user;
        String status;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            status = "login";
        } else {
            user = createUser(email, SOCIAL_GOOGLE);
            status = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        return new LoginResponse(status, accessToken);
    }

    public LoginResponse loginWithKakao(Map<String, String> request) {

        String token = request.get("access_token");
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("카카오 로그인 토큰이 없습니다.");
        }

        String email = kakaoTokenVerifier.verify(token);
        Optional<User> optionalUser = userRepository.findByEmailAndSocialAndDeleted(email, SOCIAL_KAKAO, 'N');

        User user;
        String status;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            status = "login";
        } else {
            user = createUser(email, SOCIAL_KAKAO);
            status = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        return new LoginResponse(status, accessToken);
    }

    private User createUser(String email, String social) {
        User newUser = User.builder()
                .email(email)
                .themeId(1)
                .createdAt(LocalDateTime.now())
                .deleted('N')
                .social(social)
                .build();
        return userRepository.save(newUser);
    }
}
