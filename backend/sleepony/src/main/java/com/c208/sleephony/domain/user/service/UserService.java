package com.c208.sleephony.domain.user.service;

import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public GetUserProfileResponse getUserProfileResponse(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        return GetUserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .height(user.getHeight())
                .weight(user.getWeight())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .build();
    }

}
