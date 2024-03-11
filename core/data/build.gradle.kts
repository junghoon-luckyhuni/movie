plugins {
    id("junghoon.android.library")
    id("junghoon.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.junghoon.movie.core.data"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.turbine)
}
