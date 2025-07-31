plugins {
    id("base-android-plugin")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization.plugin)
}

android {
    namespace = "ru.itis.t_trips.navigation_api"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    //core
    implementation(projects.core.utils)

    //compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui)

    //навигация
    implementation(libs.nav.component)
    implementation(libs.kotlin.serialization.json)
}