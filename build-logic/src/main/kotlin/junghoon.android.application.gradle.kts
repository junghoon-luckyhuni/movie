import com.junghoon.movie.configureHiltAndroid
import com.junghoon.movie.configureKotestAndroid
import com.junghoon.movie.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
