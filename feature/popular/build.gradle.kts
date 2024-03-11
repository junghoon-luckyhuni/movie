plugins {
    id("junghoon.android.feature")
}

android {
    namespace = "com.junghoon.movie.feature.bookmark"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.immutable)
}
