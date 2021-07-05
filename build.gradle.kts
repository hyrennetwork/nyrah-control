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

    // waterfall-chat
    implementation("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")

    // netty
    implementation("io.netty:netty-codec:4.1.58.Final")

    // vertx
    implementation("io.vertx:vertx-core:4.0.3")

    // gson
    compileOnly("com.google.code.gson:gson:2.8.7")

    // core-shared
    implementation("net.hyren:core-shared:0.1-ALPHA")
}