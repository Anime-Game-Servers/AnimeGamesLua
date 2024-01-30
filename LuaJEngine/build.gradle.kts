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
    implementation(libs.jvm.luaj)
    compileOnly(libs.jvm.lombok)
    annotationProcessor(libs.jvm.lombok)
    implementation(libs.bundles.jvm.reflection)
    implementation(libs.jvm.logging)
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "LuaJEngine"
        }
    }
}
kotlin {
    jvmToolchain(17)
}