import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    kotlin("jvm")
}

group = "org.anime_game_servers.lua"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.anime_game_servers.lua:base-jvm:0.1")
    api("org.anime_game_servers.core:gi:0.1")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.github.davidmoten:rtree-multi:0.1")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

tasks.test {
    useJUnitPlatform()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            groupId = "org.anime_game_servers"
            artifactId = "GIlua"
        }
    }
}
