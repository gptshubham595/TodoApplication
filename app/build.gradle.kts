import java.io.FileInputStream
import java.util.Properties
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ktlint.plugin)
}

val keystorePropertiesFile = rootProject.file("secret.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.todo.todoapplication"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.todo.todoapplication"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR, DEBUGGABLE"
        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        buildConfigField("String", "API_KEY", keystoreProperties.getProperty("API_KEY"))
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
//            storeFile = file(keystoreProperties.getProperty("key.store"))
//            storePassword = System.getenv("RELEASE_KEYSTORE_ALIAS")
//            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
//            keyPassword = System.getenv("RELEASE_KEYSTORE_ALIAS")

            keyAlias = keystoreProperties.getProperty("key.alias")
            keyPassword = keystoreProperties.getProperty("key.alias.password")
            storePassword = keystoreProperties.getProperty("key.store.password")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true // enable data binding
        buildConfig = true // enable build config
    }
    buildToolsVersion = AppConfig.buildToolsVersion
    ndkVersion = AppConfig.ndkVersion

    flavorDimensions += listOf("Production")
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.andrroidXCoreKtx)
    implementation(libs.appcompat)

    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":presentation"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.fragment)

    implementation(libs.profileinstaller)

    debugImplementation(libs.leakcanary.android)
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
