package com.c208.sleephony.domain.user.repsotiry;

import com.c208.sleephony.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
