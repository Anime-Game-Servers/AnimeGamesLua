import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `version-catalog`
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.kotlin.lombok)
    alias(libs.plugins.lombok)
}

group = "org.anime_game_servers.lua"
version = libs.versions.anime.game.lua.get()

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
