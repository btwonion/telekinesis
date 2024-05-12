import dev.kikugie.stonecutter.gradle.StonecutterSettings

rootProject.name = "telekinesis"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://maven.quiltmc.org/repository/release/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.3.5"
}

//include("telekinesis-paper")
include("telekinesis-fabric")

extensions.configure<StonecutterSettings> {
    kotlinController = true
    centralScript = "build.gradle.kts"
    shared {
        versions("1.20.1", "1.20.4", "1.20.6")
        vcsVersion = "1.20.6"
    }
    create(project(":telekinesis-fabric"))
}
