plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("com.modrinth.minotaur") version "2.8.1" apply (false)
    id("com.github.breadmoirai.github-release") version "2.4.1" apply (false)

    id("fabric-loom") version "1.2-SNAPSHOT" apply (false)
    id("io.github.juuxel.loom-quiltflower") version "1.9.0" apply (false)
}

repositories {
    mavenCentral()
}