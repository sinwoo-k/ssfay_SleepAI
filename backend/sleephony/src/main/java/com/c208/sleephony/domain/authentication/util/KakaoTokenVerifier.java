package com.c208.sleephony.domain.authentication.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoTokenVerifier {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String verify(String kakaoAccessToken) {
        try {
            var headers = new org.springframework.http.HttpHeaders();
            headers.add("Authorization", "Bearer " + kakaoAccessToken);
            var entity = new org.springframework.http.HttpEntity<>(headers);

            var response = restTemplate.exchange(
                    KAKAO_USER_INFO_URL,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("kakao_account").path("email").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 인증 실패", e);
        }
    }
}
