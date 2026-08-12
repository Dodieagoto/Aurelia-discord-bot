plugins {
    kotlin("jvm") version "2.3.21"
    id("com.gradleup.shadow") version "9.2.2"
    kotlin("plugin.serialization") version "2.4.10"
}

group = "dev.dodia"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    // JDA
    implementation("net.dv8tion:JDA:6.5.0") {
        exclude(module = "opus-java")
        exclude(module = "tink")
    }
    implementation("club.minnced:jda-ktx:0.15.0")

    //.env
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")

    // DB
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.jetbrains.exposed:exposed-core:1.4.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.4.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.4.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.6")
    implementation("org.xerial:sqlite-jdbc:3.49.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}

kotlin {
    jvmToolchain(25)
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        manifest {
            attributes(
                "Main-Class" to "ru.ynovka.Main"
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }
}