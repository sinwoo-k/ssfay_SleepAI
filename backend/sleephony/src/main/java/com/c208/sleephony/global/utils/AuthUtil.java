package com.c208.sleephony.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class AuthUtil {

    public static Integer getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdStr = authentication.getName();
        return Integer.parseInt(userIdStr);
    }

    public static String toAgeGroup(Integer age) {
        if(age < 10) return "10대 미만";
        if(age < 20) return "10대";
        if(age < 30) return "20대";
        if(age < 40) return "30대";
        if(age < 50) return "40대";
        if(age < 60) return "50대";
        return "60대 이상";
    }

    public static Integer getLoginUserAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return Period.between(birthDate,today).getYears();
    }
}
