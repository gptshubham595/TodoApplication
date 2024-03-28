// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.android.test") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    id("androidx.baselineprofile") version "1.2.3" // Baseline profile plugin
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
}

allprojects { // to run the ktlint Format on all modules
    tasks.register("copyGitHooks", Copy::class) {
        description = "Copies the git hooks from /git-hooks to the .git folder."
        group = "git hooks"
        from("$rootDir/scripts/pre-commit") // make sure you pre-commit reside here
        into("$rootDir/.git/hooks/")
    }

    tasks.register("installGitHooks", Exec::class) {
        description = "Installs the pre-commit git hooks from /git-hooks."
        group = "git hooks"
        workingDir = rootDir
        commandLine("chmod", "-R", "+x", ".git/hooks/")
        dependsOn("copyGitHooks")
        doLast {
            logger.info("Git hook installed successfully.")
        }
    }
    afterEvaluate {
        tasks.findByName("preBuild")?.let {
            it.dependsOn("installGitHooks")
        }
    }
}
