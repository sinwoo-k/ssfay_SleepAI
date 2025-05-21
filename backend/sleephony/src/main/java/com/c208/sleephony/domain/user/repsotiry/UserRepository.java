package com.c208.sleephony.domain.user.repsotiry;

import com.c208.sleephony.domain.preview.dto.response.UserSummaryDto;
import com.c208.sleephony.domain.user.entity.Social;
import com.c208.sleephony.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndSocialAndDeleted(String email, Social social, char deleted);
    Optional<User> findByUserIdAndDeleted(Integer userId, char deleted);

    List<UserSummaryDto> findAllProjectedByDeleted(Character deleted);
}
