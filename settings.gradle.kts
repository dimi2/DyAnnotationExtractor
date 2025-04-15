/*
 * Main setup for the project build system - Gradle.
 */
rootProject.name = extra["APP_NAME"] as String

dependencyResolutionManagement {
    repositories {
        // Use Maven Central for resolving dependencies.
        mavenCentral()
    }
}


