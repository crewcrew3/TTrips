plugins {
    id("base-android-plugin-compose")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization.plugin)
}

android {
    namespace = "ru.itis.t_trips.ui"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

//не получилось вынести в конвеншн плагин
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    //modules: core
    implementation(projects.core.navigationApi) //для bottom nav(чтобы она знала о routes, а они ведь в том модуле)
    implementation(projects.core.utils)

    //навигация
    implementation(libs.nav.component)
    implementation(libs.kotlin.serialization.json)
    //Изображения
    implementation(libs.bundles.coil)
}