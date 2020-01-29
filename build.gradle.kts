import com.android.build.gradle.BaseExtension
import groovy.util.Node
import groovy.util.XmlParser
import kotlin.math.roundToInt

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
    }

    if ((group as String).isNotEmpty()) {
        if (name == "app")
            configureAndroid()

        if (name == "android-client")
            configureAndroidLibs()

        if (name == "model" || name == "api")
            configureKotlin()

        configureJacoco()
    }
}


fun Project.configureJacoco() {
    apply(plugin = "jacoco")

    task("jacocoTestReport", JacocoReport::class) {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
            html.destination = file("${buildDir}/jacocoHtml")
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "**/*Activity*.*",
            "**/*Fragment*.*",
            "**/*Module.*",
            "**/Mock*.*",
            "android/**/*.*"
        )
        val javaSourceFiles = fileTree(baseDir = "${buildDir}/intermediates/classes/debug")
            .exclude(fileFilter)
        val kotlinSourceFiles = fileTree(baseDir = "$buildDir/tmp/kotlin-classes")
            .exclude(fileFilter)
        val mainSrc = "${project.projectDir}/src/main/java"
        val executionData = fileTree(baseDir = "$buildDir")
            .setIncludes(
                listOf(
                    "jacoco/testDebugUnitTest.exec",
                    "outputs/code-coverage/connected/*coverage.ec"
                )
            )


        additionalSourceDirs(files(listOf(mainSrc)))
        additionalClassDirs(files(listOf(javaSourceFiles, kotlinSourceFiles)))
        executionData(executionData)
    }.dependsOn("testDebugUnitTest").doLast {
        val reportDir =
            project.extensions.getByType(JacocoPluginExtension::class).reportsDir.absoluteFile
        val report = file("$reportDir/jacocoTestReport/jacocoTestReport.xml")

        val parser = XmlParser()
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)

        val result = parser.parse(report)
        val children = result.get("counter")
        val validCounterType =
            listOf("INSTRUCTION", "BRANCH", "LINE", "COMPLEXITY", "METHOD", "CLASS")

        if (children is List<*>) {
            children.forEach {
                if (it is Node && it.attribute("type") == "INSTRUCTION") {
                    println("Coverage")
                    val covered = (it.attribute("covered") as String).toDouble()
                    val missed = (it.attribute("missed") as String).toDouble()
                    val percentage = ((covered / (covered + missed)) * 100).toFloat()
                    println("${project.name} | %.2f / %.2f | %.2f%s".format(covered, missed, percentage, "%"))
                }
            }
        }


    }
}


fun Project.configureKotlin() {
    apply(plugin = "kotlin")
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
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}