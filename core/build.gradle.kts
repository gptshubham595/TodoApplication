import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.baselineprofile")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.realm.kotlin")
}

android {
    namespace = "com.todo.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    api(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("org.greenrobot:eventbus:3.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")

    implementation("androidx.security:security-crypto:1.0.0")

    implementation("io.realm.kotlin:library-base:1.13.0")
    implementation("io.realm.kotlin:library-sync:1.13.0")

    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")

    // Room Database Testing: used for testing Room database
    testImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")

    implementation("com.squareup.retrofit2:converter-simplexml:2.6.1")

    // Retrofit Mockito: used for mocking Retrofit responses
    androidTestImplementation("com.squareup.retrofit2:retrofit-mock:2.9.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    // Mockito: used for mocking objects in tests
    androidTestImplementation("org.mockito:mockito-core:5.7.0")

    // Robolectric: used for testing Android components without needing a device
    androidTestImplementation("org.robolectric:robolectric:4.12")

    // Google Truth: used for more readable assertions in tests
    androidTestImplementation("com.google.truth:truth:1.1.3")

    // livedata testing : used for testing livedata
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2")
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
