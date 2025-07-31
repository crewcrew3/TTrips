plugins {
    id("base-android-plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.domain"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    //modules: core
    implementation(projects.core.utils)

    //корутины
    implementation(libs.coroutines.core)
    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}