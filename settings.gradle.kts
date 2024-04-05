pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
rootProject.name = "TodoApplication"
include(":app")
include(":macro_benchmark")
include(":presentation")
include(":core")
include(":data")
include(":domain")
include(":common")
