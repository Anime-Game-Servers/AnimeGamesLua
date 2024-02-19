plugins {
    `version-catalog`
    kotlin("multiplatform")
}

group = "org.anime_game_servers.lua"
version = libs.versions.anime.game.lua.get()

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
    mingwX64()
    linuxX64()
    linuxArm64()


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ags.core.base)
                implementation(libs.kotlin.reflect)
                implementation(libs.logging)
                api(libs.kotlinx.io.core)
            }
        }
        val commonTest by getting {
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.jvm.ags.core.base)
                implementation(libs.bundles.jvm.reflection)
                implementation(libs.findbugs.jsr305)
                implementation(libs.jvm.logging)
                api(libs.kotlinx.io.core)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(project(":base"))
                implementation(project(":LuaJEngine"))
                implementation(project(":JNLuaEngine"))
            }

        }
        val jsMain by getting
        val jsTest by getting
    }
}