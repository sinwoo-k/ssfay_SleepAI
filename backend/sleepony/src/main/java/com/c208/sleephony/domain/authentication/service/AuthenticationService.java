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

    public LoginResponse  loginWithGoogle(Map<String, String> request) {
        String token = request.get("token");

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("구글 로그인 토큰이 없습니다.");
        }

        GoogleIdToken.Payload payload = googleTokenVerifier.verify(token);
        String email = payload.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        Integer userId;
        String status;

        if (optionalUser.isPresent()) {
            userId = optionalUser.get().getUserId();
            status = "login";
        } else {
            User newUser = User.builder()
                    .email(email)
                    .themeId(1)
                    .createdAt(LocalDateTime.now())
                    .deleted('N')
                    .build();
            userRepository.save(newUser);
            userId = newUser.getUserId();
            status = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(userId);
        return new LoginResponse(status, accessToken);
    }
}
