package com.c208.sleephony.domain.user.service;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 로그인한 사용자의 프로필을 최초로 등록합니다.
     *
     * @param request 사용자 프로필 생성 요청 DTO
     */
    public void createUserProfile (CreateUserProfileRequest request){
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    /**
     * 로그인한 사용자의 프로필 정보를 수정합니다.
     *
     * @param request 사용자 프로필 수정 요청 DTO
     */
    public void updateUserProfile(UpdateUserProfileRequest request) {
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    /**
     * 로그인한 사용자의 프로필 정보를 조회합니다.
     *
     * @return GetUserProfileResponse 프로필 정보 DTO
     */
    @Transactional(readOnly = true)
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

    /**
     * 로그인한 사용자를 탈퇴 처리합니다.
     * 실제 삭제가 아닌 'deleted' 플래그를 'Y'로 설정하여 논리적 삭제를 수행합니다.
     */
    public void deleteUser() {
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: id = " + userId));

        user.setDeleted('Y');
    }


}
