@file:Suppress("SpellCheckingInspection")

import org.gradle.api.artifacts.ArtifactRepositoryContainer.MAVEN_CENTRAL_URL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readText

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
val majorVersion = "2.3.0"
val mcVersion = "1.20.1"
version = "$majorVersion-$mcVersion"
description = "Adds a telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://maven.parchmentmc.org/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.22")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.10+kotlin.1.9.10")
    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.0.2+1.20")
    modImplementation("com.terraformersmc:modmenu:7.1.0")

    implementation("com.akuleshov7:ktoml-core-jvm:0.5.0")
    include("com.akuleshov7:ktoml-core-jvm:0.4.1")

    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.2.0-rc.2")!!)!!)
    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.0.2")!!)!!)

    // Integration
    modImplementation("maven.modrinth:abooMhox:c2klaSgQ") // tree-harvester by ricksouth
    modImplementation("maven.modrinth:MpzVLzy5:9kJblF2V") // better nether by quickueck
    modImplementation("maven.modrinth:EFtixeiF:Mk1aTgPH") // levelz by Globox1997
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
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}

val changelogFile: Path = rootDir.toPath().resolve("changelogs/fabric-$version.md")
val changelogText = if (changelogFile.notExists()) "" else changelogFile.readText()
val projectVersionname = "fabric-${project.version}"

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set(projectVersionname)
    versionName.set(projectVersionname)
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(mcVersion))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-language-kotlin")
        required.project("yacl")
        required.project("modmenu")
    }
    changelog.set(changelogText)
    syncBodyFrom.set(file("../README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner(split[0])
    repo(split[1])
    releaseName(projectVersionname)
    tagName(projectVersionname)
    body(changelogText)
    targetCommitish("master")
    setReleaseAssets(tasks["remapJar"])
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
            version = "${project.version}"
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