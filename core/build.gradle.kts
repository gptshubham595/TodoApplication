import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.library)
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.baselineprofile.plugin)
    alias(libs.plugins.ktlint.plugin)
    alias(libs.plugins.realm.plugin)
}

android {
    namespace = "com.todo.core"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        consumerProguardFiles("consumer-rules.pro")
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
    kotlin {
        jvmToolchain(17)
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        unitTests.all {
            it.systemProperty("robolectric.logging.enabled", "false")
        }
    }

    baselineProfile {
        // Filters the generated profile rules (optional)
        // This example keeps the classes in the `com.example.mylibrary.**` package and its subpackages.
        filter {
            include("com.todo.core.**")
        }
    }
}

dependencies {

    implementation(libs.andrroidXCoreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    api(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
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

    implementation(libs.androidx.security.crypto)

    implementation(libs.realm.library.base)
    implementation(libs.realm.library.sync)

    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)

    // Room Database Testing: used for testing Room database
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.room.room.testing)

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
