plugins {
    id("junghoon.android.library")
    id("junghoon.android.compose")
}

android {
    namespace = "com.junghoon.movie.core.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)

    implementation(libs.landscapist.bom)
    implementation(libs.landscapist.coil)
    implementation(libs.landscapist.placeholder)
}
