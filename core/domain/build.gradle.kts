
plugins {
    id("junghoon.android.library")
}

android {
    namespace = "com.junghoon.movie.core.domain"
}

dependencies {
    implementation(libs.inject)
    implementation(libs.androidx.lifecycle.runtimeCompose)
}
