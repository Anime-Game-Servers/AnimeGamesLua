import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `version-catalog`
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.kotlin.lombok)
    alias(libs.plugins.lombok)
}

val baseVersion = libs.versions.anime.game.lua.get()
val versionSuffix = System.getenv("VERSION_SUFFIX")?.let{"-$it"} ?: ""
version = "$baseVersion$versionSuffix"
group = "org.anime_game_servers.lua"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit}"))
    testImplementation(libs.junit)
    implementation(libs.jvm.kotlin.stdlib)
    api(libs.bundles.jvm.ags.lua.gi)
    implementation(libs.findbugs.jsr305)
    implementation(libs.jvm.logging)
    compileOnly(libs.jvm.lombok)
    annotationProcessor(libs.jvm.lombok)
    implementation(libs.jvm.rtree.multi)
    implementation(project(":base"))
    testImplementation(project(":LuaJEngine"))
    testImplementation(project(":JNLuaEngine"))
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
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
            groupId = "org.anime_game_servers.lua"
            artifactId = "GIlua"
        }
    }
}
