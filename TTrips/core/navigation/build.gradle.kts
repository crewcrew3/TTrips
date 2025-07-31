plugins {
    id("base-android-plugin-compose")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "ru.itis.t_trips.navigation"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    //modules: core
    implementation(projects.core.navigationApi) //для того чтобы реалтзовать -Navigator интерфейсы
    //modules: features (для AppNavHost)
    implementation(projects.feature.authentication)
    implementation(projects.feature.triplist)
    implementation(projects.feature.profile)
    implementation(projects.feature.triparchive)
    implementation(projects.feature.editprofile)
    implementation(projects.feature.addtrip)
    implementation(projects.feature.tripinfo)
    implementation(projects.feature.looktripmembers)
    implementation(projects.feature.notifications)
    implementation(projects.feature.invitationdetails)
    implementation(projects.feature.addactualexpense)
    implementation(projects.feature.actualexpenseinfo)
    implementation(projects.feature.addplannedexpense)
    implementation(projects.feature.reporttrip)

    //навигация
    implementation(libs.nav.component)
    implementation(libs.kotlin.serialization.json)
    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}