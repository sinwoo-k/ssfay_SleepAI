package com.c208.sleephony.domain.user.service;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
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

    // 유저 프로필 등록
    public void createUserProfile (Integer userId, CreateUserProfileRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    // 유저 프로필 수정
    public void updateUserProfile(Integer userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    // 유저 프로플 조회
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
