import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.readText

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0"

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.1.1"
version = "paper-$majorVersion-1.20.1"
description = "Adds an telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":telekinesis-common", configuration = "namedElements"))
    paperweight.foliaDevBundle("1.20.1-R0.1-SNAPSHOT")
}

tasks {
    register("releasePlugin") {
        group = "publishing"

        dependsOn("modrinth")
        dependsOn("publish")
        dependsOn("modrinthSyncBody")
        dependsOn("githubRelease")
    }

    val kotlinVersion: String by project
    processResources {
        val props = mapOf(
            "name" to "telekinesis",
            "version" to project.version.toString(),
            "main" to "dev.nyon.telekinesis.Main",
            "description" to project.description,
            "website" to "https://nyon.dev/discord",
            "apiVersion" to "1.20",
            "authors" to projectAuthors.joinToString("\n  - ", "\n  - "),
            "foliaSupported" to true,
            "libraries" to listOf(
                "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
                "com.akuleshov7:ktoml-core-jvm:0.5.0"
            ).joinToString("\n  - ", "\n  - ")
        )

        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
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

    assemble {
        dependsOn(shadowJar)
        dependsOn(reobfJar)
    }

    runPaper.folia.registerTask()
    runServer {
        minecraftVersion("1.20.1")
    }
}

val changelogText = rootDir.toPath().resolve("changelogs/$version.md").readText()

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    uploadFile.set(tasks["assemble"])
    gameVersions.set(listOf("1.20", "1.20.1"))
    loaders.set(listOf("paper", "folia"))
    changelog.set(changelog)
    syncBodyFrom.set(file("../README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner(split[0])
    repo(split[1])
    tagName("${project.version}")
    body(changelogText)
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