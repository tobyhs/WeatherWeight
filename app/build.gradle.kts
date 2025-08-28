import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

val secretProps = Properties()
rootProject.file("secret.properties").inputStream().use { secretProps.load(it) }

android {
    namespace = "io.github.tobyhs.weatherweight"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.tobyhs.weatherweight"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ACCUWEATHER_API_KEY", "\"${secretProps["accuweatherApiKey"]}\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            versionNameSuffix = "-debug"
        }

        create("local") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "-local"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    testImplementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.activity.compose)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
    implementation(libs.hilt.android)
    testImplementation(libs.hilt.android.testing)

    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    ksp(libs.retrofit.responseTypeKeeper)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.espresso.core)
    testImplementation(libs.espresso.intents)
    testImplementation(libs.androidx.junit)

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}
