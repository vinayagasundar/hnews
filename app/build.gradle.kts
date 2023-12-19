plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
//    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk=34

    defaultConfig {
        namespace="com.blackcatz.android.hnews"

        applicationId="com.blackcatz.android.hnews"
        minSdk=21
        targetSdk=33
        versionCode=1
        versionName="1.0"

        vectorDrawables {
            useSupportLibrary=true
        }

//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += ["room.schemaLocation": "$projectDir/schemas".toString()]
//            }
//        }
    }

//    buildTypes {
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
    compileOptions {
        sourceCompatibility=JavaVersion.VERSION_1_8
        targetCompatibility=JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose=true
        buildConfig=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion=libs.versions.compose.get()
    }
    packagingOptions {
        resources {
//            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}

dependencies {
    implementation(libs.core)
    implementation(libs.lifecycle.ktx)
    implementation(libs.lifecycle.compose.viewmodel)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.material)
    implementation(libs.bundles.room)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.dagger.core)
    implementation(libs.coroutine.android)
    implementation(libs.timberLog)
    implementation(libs.rxjava.android)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.extension)
    implementation("com.android.support:customtabs:28.0.0")
    kapt(libs.dagger.core.compiler)
    kapt(libs.room.compiler)
    debugImplementation(libs.compose.debug.tool)
}

// dependencies {
//     implementation(AppConfig.Libs.Kotlin.coroutineAndroid)
//     implementation(AppConfig.Libs.Support.appCompat)
//     implementation(AppConfig.Libs.Support.constraitLayout)

//     implementation(AppConfig.Libs.Support.materialDesign)
//     implementation(AppConfig.Libs.Support.recyclerView)

//     implementation(AppConfig.Libs.Support.lifeCycle)
//     implementation(AppConfig.Libs.Support.customTabs)

//     implementation(AppConfig.Libs.Dagger.daggerAndroid)
//     implementation(AppConfig.Libs.Dagger.daggerAndroidSupport)

//     implementation(AppConfig.Libs.Network.retrofit2)
//     implementation(AppConfig.Libs.Network.okhttpLogger)
//     implementation(AppConfig.Libs.Network.gsonConv)
//     implementation(AppConfig.Libs.Network.rxJavaAdapter)

//     implementation(AppConfig.Libs.RxJava.rxJava)
//     implementation(AppConfig.Libs.RxJava.rxJavaAndroid)

//     implementation(AppConfig.Libs.Debug.timberLogger)

//     kapt(AppConfig.Libs.Dagger.daggerCompiler)
//     kapt(AppConfig.Libs.Dagger.daggerAndroidCompiler)

//     testImplementation(AppConfig.Libs.Test.junit)
//     testImplementation(AppConfig.Libs.Support.roomTestHelper)
//     testImplementation(AppConfig.Libs.Test.Mockito.nhaarmanMock)
//     testImplementation(AppConfig.Libs.Network.retrofit2Mock)

//     // androidTestImplementation(AppConfig.Libs.AndroidTest.testRunner)
//     // androidTestImplementation(AppConfig.Libs.AndroidTest.espressoCore)
// }
