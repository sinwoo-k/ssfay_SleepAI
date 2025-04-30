package com.c208.sleephony.domain.user.repsotiry;

import com.c208.sleephony.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndSocialAndDeleted(String email, String social, char deleted);
    Optional<User> findByUserIdAndDeleted(Integer userId, char deleted);

}
