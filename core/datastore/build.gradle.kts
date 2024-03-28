plugins {
    id("junghoon.android.library")
}

android {
    namespace = "com.junghoon.movie.core.datastore"
}

dependencies {
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.test)
    implementation(libs.androidx.datastore)
}
