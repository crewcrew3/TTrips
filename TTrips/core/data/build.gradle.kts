plugins {
    id("base-android-plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "DATABASE_NAME",
            "\"LOCAL_DATABASE\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    //modules: core
    implementation(projects.core.network) //интерфейс TokenStorage+response-модельки
    implementation(projects.core.domain) //отсюда нужны интерфейсы репо+бизнес модельки(для мапперов)
    implementation(projects.core.utils)

    //бд
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.ksp)
    //ретрофит(для HttpException)
    implementation(libs.retrofit)
    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    //datasore
    implementation(libs.datastore.prefs)
}