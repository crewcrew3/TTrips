plugins {
    id("base-android-plugin-compose")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.addactualexpense"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    //modules: core
    implementation(projects.core.navigationApi)
    implementation(projects.core.ui) //ui
    implementation(projects.core.domain) //usecases, exceptions
    implementation(projects.core.common) //для обработчика ошибок
    implementation(projects.core.utils)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    //корутины
    implementation(libs.coroutines.core)
    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.nav.compose)
    //сериализация
    implementation(libs.kotlin.serialization.json)
}