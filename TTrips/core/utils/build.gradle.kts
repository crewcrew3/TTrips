plugins {
    id("base-android-plugin")
}

android {
    namespace = "ru.itis.t_trips.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}