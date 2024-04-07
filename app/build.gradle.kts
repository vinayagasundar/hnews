plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id(libs.plugins.androidx.room.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    compileSdk = 34

    defaultConfig {
        namespace = "com.blackcatz.android.hnews"

        applicationId = "com.blackcatz.android.hnews"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
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
    implementation(libs.compose.material.icon)

    debugImplementation(libs.compose.debug.tooling)

    implementation(libs.core)
    implementation(libs.lifecycle.ktx)
    implementation(libs.lifecycle.compose.viewmodel)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.material)
    implementation(libs.bundles.room)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.dagger.hilt)
    implementation(libs.coroutine.android)
    implementation(libs.timberLog)
    implementation(libs.rxjava.core)
    implementation(libs.rxjava.android)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.extension)
    implementation("com.android.support:customtabs:28.0.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    debugImplementation(libs.ui.test.manifest)
    kapt(libs.dagger.hilt.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
}