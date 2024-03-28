plugins {
    id("junghoon.android.library")
    id("junghoon.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.junghoon.movie.core.data"

    defaultConfig {
        buildConfigField("String", "API_DOMAIN", "\"https://api.themoviedb.org/3/\"")
        buildConfigField("String", "API_KEY", "\"515cc35a4a5a0d8846cea73d1de167bf\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.datastore)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    testImplementation(projects.core.testing)
}
