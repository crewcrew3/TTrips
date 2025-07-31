import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = AndroidConst.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidConst.MIN_SDK
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }
    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    //compose
    implementation(libs.bundles.compose.deps)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

//что бы в этом файле мы могли обращаться к libs
internal val Project.libs: LibrariesForLibs
    get() = (this as ExtensionAware).extensions.getByName("libs") as LibrariesForLibs

//для kotlinOptions
fun com.android.build.gradle.internal.dsl.BaseAppModuleExtension.`kotlinOptions`(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlinOptions", configure)

//android { ... }
val org.gradle.api.Project.`android`: com.android.build.gradle.internal.dsl.BaseAppModuleExtension get() =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.gradle.internal.dsl.BaseAppModuleExtension

