// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    dependencyLocking {
        lockAllConfigurations()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}
