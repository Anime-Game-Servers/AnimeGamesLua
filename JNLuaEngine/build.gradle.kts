import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `version-catalog`
    `java-library`
    kotlin("jvm")
}

group = "org.anime_game_servers.lua"
version = libs.versions.anime.game.lua.get()

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit}"))
    testImplementation(libs.junit)
    implementation(libs.jvm.kotlin.stdlib)
    api(libs.bundles.jvm.ags.lua.engine)
    implementation(libs.findbugs.jsr305)
    implementation(libs.jvm.jnlua)
    compileOnly(libs.jvm.lombok)
    annotationProcessor(libs.jvm.lombok)
    implementation(libs.bundles.jvm.reflection)
    implementation(libs.jvm.logging)
    implementation(libs.jvm.kotlinx.io.core)
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
            from(components["java"])
            artifactId = "JNLuaEngine"
        }
    }
}
