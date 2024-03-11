plugins {
    id("junghoon.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.junghoon.movie"

    defaultConfig {
        applicationId = "com.junghoon.movie"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.feature.main)
    implementation(projects.core.data)
}
