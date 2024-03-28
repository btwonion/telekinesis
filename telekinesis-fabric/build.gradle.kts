@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.readText

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("fabric-loom")

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.4.0"
val mcVersion = "24w13a"
val supportedMcVersions = listOf(mcVersion)
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
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(
        loom.layered {
            parchment("org.parchmentmc.data:parchment-1.20.4:2024.02.25@zip")
            officialMojangMappings()
        },
    )
    implementation("org.vineflower:vineflower:1.9.3")
    modImplementation("net.fabricmc:fabric-loader:0.15.7")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.19+kotlin.1.9.23")
    modCompileOnly("dev.isxander.yacl:yet-another-config-lib-fabric:3.3.1+1.20.4")
    modImplementation("com.terraformersmc:modmenu:10.0.0-alpha.3")

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:0.96.13+1.20.5")

    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.1.1")!!)!!)

    include(implementation("com.akuleshov7:ktoml-core-jvm:0.5.1")!!)

    // Integration
    // modCompileOnly("maven.modrinth:abooMhox:c2klaSgQ") // tree-harvester by ricksouth wait for 1.20.5
    // modCompileOnly("maven.modrinth:MpzVLzy5:9kJblF2V") // better nether by quickueck wait for 1.20.5
    // modCompileOnly("maven.modrinth:EFtixeiF:Gcai736Z") // levelz by Globox1997 wait for 1.20.5
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

    loom {
        accessWidenerPath = file("src/main/resources/telekinesis.accesswidener")
    }
}

val changelogText =
    buildString {
        append("# v${project.version}\n")
        rootDir.toPath().resolve("changelog.md").readText().also { append(it) }
    }

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set("v${project.version}")
    versionName.set("v${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(supportedMcVersions)
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

    val (rowner, rrepo) = githubRepo.split("/")
    owner = rowner
    repo = rrepo
    releaseName = "v${project.version}"
    tagName = "v${project.version}"
    body = changelogText
    targetCommitish = "master"
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
