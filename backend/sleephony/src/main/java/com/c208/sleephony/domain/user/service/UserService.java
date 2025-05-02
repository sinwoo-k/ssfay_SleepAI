package com.c208.sleephony.domain.user.service;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // 유저 프로필 등록
    public void createUserProfile (CreateUserProfileRequest request){
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getHeight() != null) user.setHeight(request.getHeight());
        if (request.getWeight() != null) user.setWeight(request.getWeight());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) user.setGender(request.getGender());

        userRepository.save(user);
    }

    // 유저 프로필 수정
    public void updateUserProfile(UpdateUserProfileRequest request) {
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getHeight() != null) user.setHeight(request.getHeight());
        if (request.getWeight() != null) user.setWeight(request.getWeight());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) user.setGender(request.getGender());

        userRepository.save(user);
    }

    // 유저 프로플 조회
    public GetUserProfileResponse getUserProfileResponse(){
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
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

    // 유저 삭제
    public void deleteUser() {
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setDeleted('Y');
        user.setNickname("탈퇴한 사용자");
        user.setEmail("deleted_" + userId + "@sleephony.com");
        user.setBirthDate(LocalDate.of(1900, 1, 1));
        user.setGender("X");
        user.setHeight((float) -1.0);
        user.setWeight((float) 0);
    }


}
