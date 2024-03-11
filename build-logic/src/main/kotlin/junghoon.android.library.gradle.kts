import com.junghoon.movie.configureCoroutineAndroid
import com.junghoon.movie.configureHiltAndroid
import com.junghoon.movie.configureKotest
import com.junghoon.movie.configureKotlinAndroid

plugins {
    id("com.android.library")
    id("junghoon.verify.detekt")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()
