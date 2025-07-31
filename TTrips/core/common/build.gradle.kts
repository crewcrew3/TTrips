plugins {
    id("base-android-plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.common"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    //modules: core
    implementation(projects.core.navigationApi)
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}