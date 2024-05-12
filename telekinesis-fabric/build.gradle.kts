@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.readText

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("fabric-loom") version "1.6-SNAPSHOT"

    id("me.modmuss50.mod-publish-plugin") version "0.5.+"

    `maven-publish`
    signing
}

val featureVersion = "2.4.1"
val mcVersion = property("mcVersion")!!.toString()
val mcVersionRange = property("mcVersionRange")!!.toString()
version = "$featureVersion-$mcVersion"

group = "dev.nyon"
val projectAuthors = listOf("btwonion")
val githubRepo = "btwonion/telekinesis"

loom {
    if (stonecutter.current.isActive) {
        runConfigs.all {
            ideConfigGenerated(true)
            runDir("../../run")
        }

        project.tasks.register("runActive") {
            group = "mod"

            dependsOn(tasks.named("runClient"))
        }
    }

    mixin { useLegacyMixinAp = false }

    accessWidenerPath = file("../../src/main/resources/telekinesis.accesswidener")
}

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
    maven("https://maven.terraformersmc.com")
    maven("https://maven.parchmentmc.org")
    maven("https://repo.nyon.dev/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(
        loom.layered {
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
            officialMojangMappings()
        }
    )

    implementation("org.vineflower:vineflower:1.10.1")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fapi")!!}")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.20+kotlin.1.9.24")

    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")!!}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modMenu")!!}")

    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.1.1")!!)!!)

    include(implementation("com.akuleshov7:ktoml-core-jvm:0.5.1")!!)

    // Integration
    // modCompileOnly("maven.modrinth:abooMhox:c2klaSgQ") // tree-harvester by ricksouth wait for 1.20.5
    // modCompileOnly("maven.modrinth:MpzVLzy5:9kJblF2V") // better nether by quickueck wait for 1.20.5
    // modCompileOnly("maven.modrinth:EFtixeiF:Gcai736Z") // levelz by Globox1997 wait for 1.20.5
}

val javaVersion = property("javaVer")!!.toString()
tasks {
    processResources {
        val modId = "telekinesis"
        val modName = "telekinesis"
        val modDescription = "Adds a telekinesis enchantment to minecraft"

        val props =
            mapOf(
                "id" to modId,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubRepo,
                "mc" to mcVersionRange
            )

        props.forEach(inputs::property)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("publishMods")
        dependsOn("publish")
    }

    withType<JavaCompile> {
        options.release = javaVersion.toInt()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }
}

val changelogText =
    buildString {
        append("# v${project.version}\n")
        file("../../../changelog.md").readText().also { append(it) }
    }

val supportedMcVersions: List<String> = property("supportedMcVersions")!!.toString().split(',').map(String::trim).filter(String::isNotEmpty)

publishMods {
    displayName = "v${project.version}"
    file = tasks.remapJar.get().archiveFile
    changelog = changelogText
    type = STABLE
    modLoaders.addAll("fabric", "quilt")

    modrinth {
        projectId = "LLfA8jAD"
        accessToken = providers.environmentVariable("MODRINTH_API_KEY")
        minecraftVersions.addAll(supportedMcVersions)

        requires { slug = "fabric-api" }
        requires { slug = "yacl" }
        requires { slug  = "fabric-language-kotlin" }
        optional { slug = "modmenu" }
    }

    github {
        repository = githubRepo
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        commitish = "master"
    }

    discord {
        webhookUrl = providers.environmentVariable("DISCORD_WEBHOOK")
        username = "Release Notifier"
        content = "# A new version of Telekinesis released!\n$changelogText"
    }
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials {
                username = providers.environmentVariable("NYON_USERNAME").orNull
                password = providers.environmentVariable("NYON_PASSWORD").orNull
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.nyon"
            artifactId = "telekinesis"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()

    javaVersion.toInt()
        .let { JavaVersion.values()[it - 1] }
        .let {
            sourceCompatibility = it
            targetCompatibility = it
        }
}

kotlin {
    jvmToolchain(javaVersion.toInt())
}

/*
signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useGpgCmd()
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
    sign(publishing.publications)
}
 */

