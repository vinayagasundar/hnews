plugins {
    id("com.android.application")
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.compose.compiler.get().pluginId)
}

android {

    compileSdk = 37
    defaultConfig {
        namespace = "com.blackcatz.android.hnews"

        applicationId = "com.blackcatz.android.hnews"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles.addAll(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            )
        }
    }

    sourceSets {
        getByName("test") {
            java.srcDirs(listOf(file("src/commonTest/java")))
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)

    debugImplementation(libs.compose.debug.tooling)

    implementation(libs.core)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.compose.viewmodel)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.dagger.hilt)
    implementation(libs.coroutine.android)
    implementation(libs.timberLog)
    ksp(libs.dagger.hilt.compiler)
}