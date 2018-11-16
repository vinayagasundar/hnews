import org.jetbrains.kotlin.config.AnalysisFlag.Flags.experimental

android {
    defaultConfig {
        applicationId = "com.blackcatz.android.hnews"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    androidExtensions {
        isExperimental = true
    }
}

dependencies {
    implementation(AppConfig.Libs.Kotlin.jvm)
    implementation(AppConfig.Libs.Kotlin.coroutineAndroid)
    implementation(AppConfig.Libs.Support.appCompat)
    implementation(AppConfig.Libs.Support.constraitLayout)

    implementation(AppConfig.Libs.Support.materialDesign)
    implementation(AppConfig.Libs.Support.recyclerView)


    implementation(AppConfig.Libs.Support.lifeCycle)
    implementation(AppConfig.Libs.Support.room)

    implementation(AppConfig.Libs.BlackCatz.daggerbase)

    implementation(AppConfig.Libs.Dagger.daggerAndroid)
    implementation(AppConfig.Libs.Dagger.daggerAndroidSupport)

    implementation(AppConfig.Libs.Network.retrofit2)
    implementation(AppConfig.Libs.Network.okhttpLogger)
    implementation(AppConfig.Libs.Network.gsonConv)
    implementation(AppConfig.Libs.Network.rxJavaAdapter)
    implementation(AppConfig.Libs.Image.picasso)

    implementation(AppConfig.Libs.RxJava.rxJava)
    implementation(AppConfig.Libs.RxJava.rxJavaAndroid)

    debugImplementation(AppConfig.Libs.Debug.dbDebug)


    kapt(AppConfig.Libs.Dagger.daggerCompiler)
    kapt(AppConfig.Libs.Dagger.daggerAndroidCompiler)
    kapt(AppConfig.Libs.Support.roomCompiler)

    testImplementation(AppConfig.Libs.Test.junit)
    testImplementation(AppConfig.Libs.Support.roomTestHelper)
    testImplementation(AppConfig.Libs.Test.Mockito.nhaarmanMock)
    testImplementation(AppConfig.Libs.Network.retrofit2Mock)

    androidTestImplementation(AppConfig.Libs.AndroidTest.testRunner)
    androidTestImplementation(AppConfig.Libs.AndroidTest.espressoCore)
}
