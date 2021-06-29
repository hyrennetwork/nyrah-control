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

    // netty
    implementation("io.netty:netty-codec:4.1.58.Final")

    // vertx
    implementation("io.vertx:vertx-core:4.0.3")

    // trove4j
    implementation("net.sf.trove4j:trove4j:3.0.3")

    // core-shared
    implementation("net.hyren:core-shared:0.1-ALPHA")
}