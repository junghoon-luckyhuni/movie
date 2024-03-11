plugins {
    id("junghoon.android.feature")
}

android {
    namespace = "com.junghoon.movie.feature.home"
}

dependencies {
    implementation(libs.kotlinx.immutable)
    implementation(libs.compose.shimmer)
}
