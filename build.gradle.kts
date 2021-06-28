plugins {
    kotlin("jvm") version "1.5.10"
}

group = "net.hyren"
version = "0.1-ALPHA"

repositories {
    mavenCentral()

    maven("https://repository.hyren.net/") {
        credentials {
            username = System.getenv("MAVEN_USERNAME")
            password = System.getenv("MAVEN_PASSWORD")
        }
    }
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-io-jvm:0.1.16")

    // netty
    implementation("io.netty:netty-codec:4.1.58.Final")

    // vertx
    implementation("io.vertx:vertx-core:4.0.3")
    implementation("io.vertx:vertx-lang-kotlin:4.1.0")

    // trove4j
    implementation("net.sf.trove4j:trove4j:3.0.3")

    // core-shared
    implementation("net.hyren:core-shared:0.1-ALPHA")
}