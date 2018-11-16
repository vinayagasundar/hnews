import com.android.build.gradle.BaseExtension
import com.android.builder.model.Dependencies
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(AppConfig.Plugins.android)
        classpath(AppConfig.Plugins.kotlin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            setUrl("https://dl.bintray.com/vinayagasundar/BlackCatz/")
        }
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }

    if ((group as String).isNotEmpty()) {
        if (name == "app")
            configureAndroid()

        if (name == "android-client")
            configureAndroidLibs()

        if (name == "model" || name == "api")
            configureKotlin()
    }
}


fun Project.configureKotlin() {
    apply(plugin = "kotlin")

    configure<KotlinProjectExtension> {
        experimental.coroutines = Coroutines.ENABLE
    }
}


fun Project.configureAndroid() {
    apply(plugin = "com.android.application")
    apply(plugin = "kotlin-android")
    apply(plugin = "kotlin-android-extensions")
    apply(plugin = "kotlin-kapt")

    configure<BaseExtension> {
        compileSdkVersion(AppConfig.SdkVersion.compile)

        defaultConfig {
            minSdkVersion(AppConfig.SdkVersion.min)
            targetSdkVersion(AppConfig.SdkVersion.target)
            versionCode = 1
            versionName = "1.0"
        }
    }

    configure<KotlinProjectExtension> {
        experimental.coroutines = Coroutines.ENABLE
    }
}

fun Project.configureAndroidLibs() {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "kotlin-android-extensions")
    apply(plugin = "kotlin-kapt")

    configure<BaseExtension> {
        compileSdkVersion(AppConfig.SdkVersion.compile)

        defaultConfig {
            minSdkVersion(AppConfig.SdkVersion.min)
            targetSdkVersion(AppConfig.SdkVersion.target)
        }
    }

    configure<KotlinProjectExtension> {
        experimental.coroutines = Coroutines.ENABLE
    }
}