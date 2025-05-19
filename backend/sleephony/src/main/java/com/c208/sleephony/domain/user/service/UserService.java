package com.c208.sleephony.domain.user.service;

import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.dto.response.UserSleepLevel;
import com.c208.sleephony.domain.user.dto.response.UserSummaryDto;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.exception.UserNotFoundException;
import com.c208.sleephony.global.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SleepLevelRepository sleepLevelRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY_PREFIX = "sleep:start:"; // 측정 중 여부·시작 시각 키

    /**
     * 로그인한 사용자의 프로필을 최초로 등록합니다.
     *
     * @param request 사용자 프로필 생성 요청 DTO
     */
    public void createUserProfile (CreateUserProfileRequest request){
        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findByUserIdAndDeleted(userId, 'N')
                .orElseThrow(() -> new UserNotFoundException(userId));

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
                .orElseThrow(() -> new UserNotFoundException(userId));

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
                .orElseThrow(() -> new UserNotFoundException(userId));

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
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setDeleted('Y');
    }

    public List<UserSummaryDto> getAllUsers() {
        return userRepository.findAllProjectedBy();
    }

    public boolean isMeasuring(Integer userId) {
        String key = REDIS_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    public List<UserSleepLevel> getRecentSleepLevels(Integer userId) {
        String key = REDIS_KEY_PREFIX + userId;
        String startStr = redisTemplate.opsForValue().get(key);
        if (startStr == null) {
            return List.of();  // 아직 측정 세션이 없거나 종료됨
        }
        LocalDateTime begin = LocalDateTime.parse(startStr);
        LocalDateTime now   = LocalDateTime.now();
        return sleepLevelRepository.findByUserIdAndMeasuredAtBetween(userId, begin, now)
                .stream()
                .map(UserSleepLevel::fromEntity)
                .toList();
    }
}
