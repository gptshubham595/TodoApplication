import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.test)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.todo.benchmark"
    compileSdk = AppConfig.compileSdk

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel2api34") {
            device = "Pixel 2"
            apiLevel = 34
            systemImageSource = "aosp-atd"
        }
    }

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = 34
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR, DEBUGGABLE"
        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.ext.junit)
    implementation(libs.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
