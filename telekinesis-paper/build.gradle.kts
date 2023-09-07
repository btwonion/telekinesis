import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import kotlin.io.path.readText

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("io.papermc.paperweight.userdev")
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
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
}

tasks {
    register("releasePlugin") {
        group = "publishing"

        dependsOn("modrinthSyncBody")
        dependsOn("modrinth")
        dependsOn("publish")
        dependsOn("githubRelease")
    }

    processResources {
        val props = mapOf(
            "name" to "telekinesis",
            "version" to "'${project.version}'",
            "main" to "dev.nyon.telekinesis.Main",
            "description" to project.description,
            "website" to "https://nyon.dev/discord",
            "apiVersion" to "'1.20'",
            "authors" to projectAuthors.joinToString("\n  - ", "\n  - "),
            "foliaSupported" to true,
            "loader" to "dev.nyon.telekinesis.PaperLoader"
        )

        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    assemble {
        dependsOn(reobfJar)
    }

    reobfJar {

    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
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
    uploadFile.set(tasks["build"])
    gameVersions.set(listOf("1.20", "1.20.1"))
    loaders.set(listOf("paper", "folia"))
    changelog.set(changelogText)
    syncBodyFrom.set(file("../README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner(split[0])
    repo(split[1])
    releaseName(project.version.toString())
    tagName(project.version.toString())
    body(changelogText)
    targetCommitish("master")
    setReleaseAssets(tasks["jar"])
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