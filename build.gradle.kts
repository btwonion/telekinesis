plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"

    id("me.modmuss50.mod-publish-plugin") version "0.5.+"

    id("fabric-loom") version "1.6-SNAPSHOT" apply (false)

    id("io.papermc.paperweight.userdev") version "1.5.5" apply (false)

    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}
