package com.example.sleephony.navigation

enum class ProfileStep(val route: String) {
    NICKNAME("profile/nickname"),   // 1. 닉네임
    BIRTHDAY("profile/birthday"),   // 2. 생년월일
    GENDER("profile/gender"),       // 3. 성별
    HEIGHT("profile/height"),       // 4. 키
    WEIGHT("profile/weight")        // 5. 몸무게
}
