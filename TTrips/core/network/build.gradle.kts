plugins {
    id("base-android-plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.network"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "TRAVEL_TBANK_BASE_URL",
            "\"http://84.201.170.110:8080/api/v1/\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    //modules: core
    implementation(projects.core.utils)

    //сеть
    implementation(libs.bundles.network.deps)
    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.nav.compose)
}