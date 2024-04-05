import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.library)
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ktlint.plugin)
    alias(libs.plugins.baselineprofile.plugin)
}

android {
    namespace = "com.todo.domain"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    baselineProfile {
        // Filters the generated profile rules (optional)
        // This example keeps the classes in the `com.example.mylibrary.**` package and its subpackages.
        filter {
            include("com.todo.domain.**")
        }
    }
}

dependencies {

    implementation(libs.andrroidXCoreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(":common"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.fragment)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.eventbus)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.profileinstaller)
}

ktlint {
    version.set("0.48.2")
    debug.set(true)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    enableExperimentalRules.set(true)
    additionalEditorconfig.set( // not supported until ktlint 0.49
        mapOf(
            "max_line_length" to "20"
        )
    )
    disabledRules.set(setOf("final-newline")) // not supported with ktlint 0.48+
    baseline.set(file("$projectDir/config/ktlint-baseline.xml"))
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
