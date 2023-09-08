rootProject.name = "telekinesis"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://maven.quiltmc.org/repository/release/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include("telekinesis-paper")
include("telekinesis-fabric")