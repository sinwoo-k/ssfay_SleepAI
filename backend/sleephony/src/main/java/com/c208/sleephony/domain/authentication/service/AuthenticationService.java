package com.c208.sleephony.domain.authentication.service;

import com.c208.sleephony.domain.authentication.dto.response.LoginResponse;
import com.c208.sleephony.domain.authentication.util.GoogleTokenVerifier;
import com.c208.sleephony.domain.authentication.util.KakaoTokenVerifier;
import com.c208.sleephony.domain.user.entity.Social;
import com.c208.sleephony.global.utils.JwtProvider;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final KakaoTokenVerifier kakaoTokenVerifier;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    /**
     * Google OAuth 토큰을 통해 로그인 또는 회원가입을 처리합니다.
     *
     * @param request 클라이언트에서 전달된 credential(토큰)을 포함한 Map
     * @return LoginResponse (status: login/join, accessToken 포함)
     */
    public LoginResponse loginWithGoogle(Map<String, String> request) {

        String token = request.get("credential");
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("구글 로그인 토큰이 없습니다.");
        }

        GoogleIdToken.Payload payload = googleTokenVerifier.verify(token);
        String email = payload.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailAndSocialAndDeleted(email, Social.GOOGLE, 'N');

        User user;
        String authStatus;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            authStatus = "login";
        } else {
            user = createUser(email, Social.GOOGLE);
            authStatus = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        return new LoginResponse(authStatus, accessToken);
    }


    /**
     * Kakao OAuth 토큰을 통해 로그인 또는 회원가입을 처리합니다.
     *
     * @param request 클라이언트에서 전달된 access_token을 포함한 Map
     * @return LoginResponse (status: login/join, accessToken 포함)
     */
    public LoginResponse loginWithKakao(Map<String, String> request) {

        String token = request.get("access_token");
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("카카오 로그인 토큰이 없습니다.");
        }

        String email = kakaoTokenVerifier.verify(token);
        Optional<User> optionalUser = userRepository.findByEmailAndSocialAndDeleted(email, Social.KAKAO, 'N');

        User user;
        String status;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            status = "login";
        } else {
            user = createUser(email, Social.KAKAO);
            status = "join";
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        return new LoginResponse(status, accessToken);
    }


    /**
     * 소셜 로그인으로 최초 가입하는 사용자를 DB에 저장합니다.
     *
     * @param email 사용자 이메일
     * @param social 소셜 플랫폼 (GOOGLE, KAKAO 등)
     * @return 저장된 User 객체
     */
    private User createUser(String email, Social social) {
        User newUser = User.builder()
                .email(email)
                .social(social)
                .build();
        return userRepository.save(newUser);
    }
}
