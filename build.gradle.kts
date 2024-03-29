plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("com.modrinth.minotaur") version "2.8.7" apply (false)
    id("com.github.breadmoirai.github-release") version "2.5.2" apply (false)

    id("fabric-loom") version "1.5-SNAPSHOT" apply (false)

    id("io.papermc.paperweight.userdev") version "1.5.5" apply (false)
}

repositories {
    mavenCentral()
}