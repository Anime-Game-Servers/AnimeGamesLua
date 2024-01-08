plugins {
    kotlin("multiplatform")
}

group = "org.anime_game_servers.lua"
version = "0.1"

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xjvm-default=all")
                }
            }
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.anime_game_servers.core:base:0.1")
                implementation(kotlin("reflect"))
            }
        }
        val commonTest by getting {
        }
        val jvmMain by getting {
            dependencies {
                api("org.anime_game_servers.core:base-jvm:0.1")
                implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
                implementation("com.esotericsoftware:reflectasm:1.11.9")
                implementation("com.google.code.findbugs:jsr305:3.0.2")
                implementation(kotlin("reflect"))
                implementation ("org.reflections:reflections:0.10.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}