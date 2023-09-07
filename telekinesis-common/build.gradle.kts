@file:Suppress("SpellCheckingInspection")

import io.papermc.paperweight.util.constants.MAVEN_CENTRAL_URL
import io.papermc.paperweight.util.includeFromDependencyNotation
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("io.papermc.paperweight.userdev")
    id("fabric-loom")

    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "2.2.1"
val mcVersion = "1.20.1"
version = "$majorVersion-$mcVersion"
description = "Adds an telekinesis enchantment to minecraft"
val authors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

repositories {
    mavenCentral()
    // Force netty to be retrieved from maven central
    maven {
        name = "Netty Maven Central"
        url = URI(MAVEN_CENTRAL_URL)
        content {
            includeGroup("io.netty")
            includeGroup("netty")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    implementation("com.akuleshov7:ktoml-core-jvm:0.5.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    withType<JavaCompile> {
        options.release.set(17)
    }

    task<Jar>("reobfdJar") {
        archiveClassifier = "reobf"
        from(reobfJar)
    }
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
            artifactId = "telekinesis-common"
            version = project.version.toString()
            from(components["java"])

            artifact(tasks["reobfdJar"])
        }
    }
}

signing {
    sign(publishing.publications)
}

java {
    withSourcesJar()
}