plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.verify.detektPlugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "junghoon.android.hilt"
            implementationClass = "com.junghoon.movie.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "junghoon.kotlin.hilt"
            implementationClass = "com.junghoon.movie.HiltKotlinPlugin"
        }
    }
}
