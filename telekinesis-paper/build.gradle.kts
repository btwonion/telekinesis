import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.johnrengelman.shadow") version "7.1.2"

    id("com.modrinth.minotaur")
    id("com.github.breadmoirai.github-release")

    id("io.papermc.paperweight.userdev") version "1.5.0"
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.0.0"
version = "$majorVersion-1.19.3"
description = "Adds an telekinesis enchantment to minecraft"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":telekinesis-common"))
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
        dependsOn("githubRelease")
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
    uploadFile.set(tasks["reobfJar"])
    gameVersions.set(listOf("1.19.3"))
    loaders.set(listOf("paper"))
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
    releaseAssets(tasks["reobfJar"].outputs.files)
    targetCommitish("master")
}

publishing {
    repositories {
        maven {
            name = "ossrh"
            credentials(PasswordCredentials::class)
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
        }
    }

    publications {
        register<MavenPublication>(project.name) {
            from(components["java"])

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = rootProject.version.toString()

            pom {
                name.set("${project.name}-paper")
                description.set(project.description)

                developers {
                    projectAuthors.forEach {
                        developer {
                            name.set(it)
                        }
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }

                url.set("https://github.com/${githubRepo}")

                scm {
                    connection.set("scm:git:git://github.com/${githubRepo}.git")
                    url.set("https://github.com/${githubRepo}/tree/main")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

java {
    withSourcesJar()
}