import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
}

group = "org.anime_game_servers"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":BaseLua"))
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.anime_game_servers:luaj:3.0.3")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.esotericsoftware:reflectasm:1.11.9")
}

tasks.test {
    useJUnitPlatform()
}