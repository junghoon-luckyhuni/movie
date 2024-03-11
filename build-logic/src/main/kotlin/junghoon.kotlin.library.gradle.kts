import com.junghoon.movie.configureKotest
import com.junghoon.movie.configureKotlin

plugins {
    kotlin("jvm")
    id("junghoon.verify.detekt")
}

configureKotlin()
configureKotest()
