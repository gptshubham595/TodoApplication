import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.baselineprofile.plugin)
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ktlint.plugin)
}

android {
    namespace = "com.todo.presentation"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR, DEBUGGABLE"
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
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
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
            include("com.todo.presentation.**")
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true // enable data binding
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        unitTests.all {
            it.systemProperty("robolectric.logging.enabled", "false")
        }
    }
}

dependencies {
    implementation(libs.andrroidXCoreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(":core"))
    implementation(project(":common"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.profileinstaller) // Optional, simplifies profile deployment
    baselineProfile(project(":macro_benchmark"))

    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.fragment)

    implementation(libs.eventbus)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.profileinstaller)

    implementation(libs.converter.simplexml)

    // Retrofit Mockito: used for mocking Retrofit responses
    androidTestImplementation(libs.retrofit.mock)
    androidTestImplementation(libs.mockwebserver)
    // Mockito: used for mocking objects in tests
    androidTestImplementation(libs.mockito.core)

    // Robolectric: used for testing Android components without needing a device
    androidTestImplementation(libs.robolectric)

    // Google Truth: used for more readable assertions in tests
    androidTestImplementation(libs.truth)

    // livedata testing : used for testing livedata
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.fragment.testing)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
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

    baseline.set(file("$projectDir/config/ktlint/baseline.xml"))
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
