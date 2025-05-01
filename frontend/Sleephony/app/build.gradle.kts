import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use(::load)
}

val kakaoNativeKey: String =
    localProps["KAKAO_NATIVE_APP_KEY"] as? String ?: error("KAKAO_NATIVE_APP_KEY가 없습니다.")


android {
    namespace = "com.example.sleephony"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sleephony"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "kakao_native_app_key", kakaoNativeKey)
        resValue("string", "kakao_redirect_url", "kakao${kakaoNativeKey}")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // wear 통신
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.1")
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")
    implementation(libs.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit & Gson & OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp           (libs.androidx.room.compiler)

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Lifecycle & Activity
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Kakao Login
    val LATEST_VERSION = "2.21.2"
    implementation("com.kakao.sdk:v2-all:${LATEST_VERSION}") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:${LATEST_VERSION}") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:${LATEST_VERSION}") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:${LATEST_VERSION}") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:${LATEST_VERSION}") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:${LATEST_VERSION}") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:${LATEST_VERSION}") // 카카오톡 인증 서비스 API 모듈

    // Test
    testImplementation            (libs.junit)
    androidTestImplementation     (libs.androidx.junit)
    androidTestImplementation     (libs.androidx.espresso.core)
    androidTestImplementation     (platform(libs.androidx.compose.bom))
    androidTestImplementation     (libs.androidx.ui.test.junit4)
    debugImplementation           (libs.androidx.ui.test.manifest)
}
