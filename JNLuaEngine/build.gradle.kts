import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `version-catalog`
    `java-library`
    kotlin("jvm")
}

val baseVersion = libs.versions.anime.game.lua.get()
val versionSuffix = System.getenv("VERSION_SUFFIX") ?: ""
version = "$baseVersion-$versionSuffix"
group = "org.anime_game_servers.lua"


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

kotlin {
    jvmToolchain(libs.versions.jvmTargetVersion.get().toInt())
}

java {
    withSourcesJar()
    //withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "JNLuaEngine"
        }
    }
}
