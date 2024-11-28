rootProject.name = "telekinesis"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5-beta.5"
}

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    }
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"
    shared {
        versions("1.20.1", "1.20.4", "1.20.6", "1.21", "1.21.2")
        vcsVersion = "1.21"
    }
    create(rootProject)
}