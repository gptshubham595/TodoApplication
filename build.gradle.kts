// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.kotlin.plugin) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.baselineprofile.plugin) // Baseline profile plugin
    alias(libs.plugins.ktlint.plugin) apply false
    alias(libs.plugins.realm.plugin)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.navgraph.safearg.plugin) apply false
}

allprojects {
    apply(plugin = "com.autonomousapps.dependency-analysis")
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
        tasks.withType<JavaCompile>().configureEach {
            sourceCompatibility = JavaVersion.VERSION_17.toString()
            targetCompatibility = JavaVersion.VERSION_17.toString()
        }

    }
}
