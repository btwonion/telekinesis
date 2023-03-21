@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("fabric-loom")
    id("io.github.juuxel.loom-quiltflower")

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.0.0"
version = "fabric-$majorVersion-1.19.4"
description = "Adds an telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
}

dependencies {
    implementation(include(project(":telekinesis-common"))!!)
    minecraft("com.mojang:minecraft:1.19.4")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.14.17")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.76.0+1.19.4")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")

    include("com.akuleshov7:ktoml-core-jvm:0.4.1")
}

tasks {
    processResources {
        val modId = "telekinesis"
        val modName = "Telekinesis"
        val modDescription = "Adds an telekinesis enchantment"

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubRepo)

        filesMatching(listOf("fabric.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubRepo,
            )
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("modrinth")
        dependsOn("modrinthSyncBody")
        dependsOn("githubRelease")
        dependsOn("publish")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xskip-prerelease-check"
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf("1.19.4-rc2"))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
    }
    changelog.set("No changelog provided")
    syncBodyFrom.set(file("../README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner(split[0])
    repo(split[1])
    tagName("v${project.version}")
    body("No changelog provided")
    targetCommitish("master")
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
            artifactId = "telekinesis-fabric"
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