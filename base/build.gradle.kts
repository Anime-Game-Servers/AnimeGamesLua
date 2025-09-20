plugins {
    `version-catalog`
    kotlin("multiplatform")
}

val baseVersion = libs.versions.anime.game.lua.get()
val versionSuffix = System.getenv("VERSION_SUFFIX") ?: ""
version = "$baseVersion-$versionSuffix"
group = "org.anime_game_servers.lua"

kotlin {
    jvmToolchain(libs.versions.jvmTargetVersion.get().toInt())
    jvm {
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
    macosArm64()
    macosX64()
    iosArm64()
    iosX64()


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
            dependencies {
                implementation(libs.kotlin.test)
            }
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
                implementation(project(":base"))
                implementation(project(":LuaJEngine"))
                implementation(project(":JNLuaEngine"))
            }

        }
        val jsMain by getting
        val jsTest by getting
    }
}