plugins {
    kotlin("jvm") version "1.5.20"

    id("com.github.johnrengelman.shadow") version "7.0.0"
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

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    shadowJar {
        manifest {
            attributes["Main-Class"] = "net.hyren.nyrah.control.NyrahControl"
        }
        
        archiveFileName.set("${project.name}.jar")
    }
}

dependencies {
    // kotlin
    compileOnly(kotlin("stdlib"))

    // waterfall-chat
    implementation("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")

    // netty
    implementation("io.netty:netty-codec:4.1.58.Final")

    // exposed
    compileOnly("org.jetbrains.exposed:exposed-dao:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jodatime:0.31.1")

    // vertx
    implementation("io.vertx:vertx-core:4.0.3")

    // google
    compileOnly("com.google.code.gson:gson:2.8.7")
    api("com.google.guava:guava:30.1.1-jre")

    // core-shared
    implementation("net.hyren:core-shared:0.1-ALPHA")
}
