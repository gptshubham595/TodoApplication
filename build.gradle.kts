// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.android.test") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    id ("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id ("com.diffplug.spotless") version "6.25.0"
}

apply(from = ("$rootDir/git-hooks.gradle.kts"))
