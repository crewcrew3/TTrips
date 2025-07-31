// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt.gradle.plugin) apply false
    alias(libs.plugins.ktlint.gradle.plugin) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    //alias(libs.plugins.kotlin.parcelize.plugin) apply false
    alias(libs.plugins.kotlin.serialization.plugin) apply false
    alias(libs.plugins.gradle.secrets.plugin) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.android.library) apply false
}

private val versionMajor = 1
private val versionMinor = 0

//записали в extra в корне проекта. В модулях обращаемся к extra, чтобы достать
val versionName by extra(initialValue = "$versionMajor.$versionMinor")
val versionCode by extra(initialValue = versionMajor * 1000 + versionMinor * 10)