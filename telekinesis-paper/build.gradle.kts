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

    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper") version "2.1.0"

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.3.0"
val mcVersion = "1.20.1"
version = "$majorVersion-$mcVersion"
description = "Adds an telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
    implementation("com.akuleshov7:ktoml-core-jvm:0.5.0")
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

val changelogFile: Path = rootDir.toPath().resolve("changelogs/paper-$version.md")
val changelogText = if (changelogFile.notExists()) "" else changelogFile.readText()
val versionName = "paper-${project.version}"

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("LLfA8jAD")
    versionNumber.set(versionName)
    versionType.set("release")
    uploadFile.set(tasks.reobfJar.get().outputJar)
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
    releaseName(versionName)
    tagName(versionName)
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