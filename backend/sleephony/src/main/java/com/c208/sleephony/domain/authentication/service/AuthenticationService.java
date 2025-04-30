package com.c208.sleephony.domain.authentication.service;

import com.c208.sleephony.domain.authentication.dto.response.LoginResponse;
import com.c208.sleephony.domain.authentication.util.GoogleTokenVerifier;
import com.c208.sleephony.global.utils.JwtProvider;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
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

    private final GoogleTokenVerifier googleTokenVerifier;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private static final String SOCIAL_GOOGLE = "GOOGLE";

    public LoginResponse loginWithGoogle(Map<String, String> request) {
        String token = request.get("token");

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("구글 로그인 토큰이 없습니다.");
        }

        GoogleIdToken.Payload payload = googleTokenVerifier.verify(token);
        String email = payload.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailAndSocial(email, SOCIAL_GOOGLE);

        User user;
        String status;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            status = "login";
        } else {
            user = createNewGoogleUser(email);
            status = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        return new LoginResponse(status, accessToken);
    }

    private User createNewGoogleUser(String email) {
        User newUser = User.builder()
                .email(email)
                .themeId(1)
                .createdAt(LocalDateTime.now())
                .deleted('N')
                .social(SOCIAL_GOOGLE)
                .build();
        return userRepository.save(newUser); // 저장 후 반환받은 엔티티 사용
    }
}
