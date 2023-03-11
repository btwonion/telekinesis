import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.johnrengelman.shadow") version "7.1.2"

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("io.papermc.paperweight.userdev") version "1.5.0"
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.0.0"
version = "paper-$majorVersion-1.19.3"
description = "Adds an telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":telekinesis-common", configuration = "namedElements"))
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    library("com.akuleshov7:ktoml-core-jvm:0.4.1")
    library(kotlin("stdlib"))
}

bukkit bukkit@{
    this@bukkit.name = "telekinesis"
    this@bukkit.version = project.version.toString()
    this@bukkit.description = project.description
    this@bukkit.website = "https://nyon.dev/discord"
    this@bukkit.main = "dev.nyon.telekinesis.Main"
    this@bukkit.apiVersion = "1.19"
    this@bukkit.authors = projectAuthors
}

tasks {
    register("releasePlugin") {
        group = "publishing"

        dependsOn("modrinth")
        dependsOn("publish")
    }

    build {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    shadowJar {
        dependencies {
            exclude {
                it.moduleGroup != "dev.nyon"
            }
        }
    }
}

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["shadowJar"])
    gameVersions.set(listOf("1.19.3"))
    loaders.set(listOf("paper"))
    changelog.set("No changelog provided")
    syncBodyFrom.set(file("../README.md").readText())
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.nyon"
            artifactId = "telekinesis-paper"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

signing {
    sign(publishing.publications)
}

java {
    withSourcesJar()
}