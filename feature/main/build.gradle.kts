plugins {
    id("junghoon.android.feature")
}

android {
    namespace = "com.junghoon.movie.feature.main"
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.detail)
    implementation(projects.feature.popular)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)
}
