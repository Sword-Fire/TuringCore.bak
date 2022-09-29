import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.7.10"
}

group = "net.geekmc.turingcore"
version = "0.1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://repo.spongepowered.org/maven") }
    maven { url = uri("https://www.jitpack.io") }
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    compileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
    compileOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.19.0")

    compileOnly("com.github.Project-Cepi:KStom:f02e4c21d4")

    compileOnly("net.kyori:adventure-text-minimessage:4.11.0")

    compileOnly("org.yaml:snakeyaml:1.32")

    compileOnly("com.github.Minestom:Minestom:-SNAPSHOT") {
        exclude("org.tinylog")
    }
    compileOnly(fileTree("libs"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}