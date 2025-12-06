# AnimeGameLua

This repository contains a library helping with handling the lua scripts for a certain anime game.
It defines models to extract data and contains definitions for interaction between lua and the server implementation.
It also makes some lua engines available for use

Usage
=====

## Include version in your project
Add the ags maven repository to your project.
For release versions use the following:
```groovy
maven {
    name "agsmvnReleases"
    url "https://mvn.animegameservers.org/releases"
}
```

For snapshots use the following:
```groovy
maven {
    name = "agsmvnSnapshots"
    url = "https://mvn.animegameservers.org/snapshots"
}
```

Then include the dependency in your project:
```groovy
implementation("org.anime_game_servers.lua:LuaJEngine:$version") // For using the LuaJ lua engine
implementation("org.anime_game_servers.lua:JNLuaEngine:$version") // For using the JNLua lua engine
implementation("org.anime_game_servers.lua:GIlua:$version") // For the definitions and logic for the GI anime game
```
of with a version toml file:
```toml
[versions]
anime_game_lua = "$version"

[libraries]
# For using the LuaJ lua engine
ags-lua_luaj = { module = "org.anime_game_servers.lua:LuaJEngine", version.ref = "anime_game_lua" }
# For using the JNLua lua engine
ags-lua_jnlua = { module = "org.anime_game_servers.lua:JNLuaEngine", version.ref = "anime_game_lua" }
# For the definitions and logic for the GI anime game
ags-lua_gilua = { module = "org.anime_game_servers.lua:GIlua", version.ref = "anime_game_lua" }
```

### Using the lua defintions
TODO

### Using the lua engines
#### Available lua engines:
##### [LuaJ](https://github.com/Hartie95/luaj) (jvm only)
Lua 5.2.x support fully written in java, containing, slight modifications for better anime game lua support 
##### [JNLua](https://github.com/Hartie95/JNLua_GC) (jvm only)
Native lua 5.2 and 5.3 implementation using jni for interfacing with the jvm.
The native libraries are compiled for windows x64/i686, linux x64/i686 aarch64/arm-none-eabi and macos x64/arm64e, through only x64 on linux and windows are actively tested. 

TODO


Development
=====

## How to build it for the local maven repo
* All:    `gradlew publishToMavenLocal`
* JVM:    `graldew publishJvmPublicationToMavenLocal`
* JS:     `gradlew publishJsPublicationToMavenLocal`
* Native: `gradlew publishNativePublicationToMavenLocal`

## How to generate a documentation
You can generate a documentation if the models from their kdoc.
This will be output in `build/dokka/html`
```sh
gradlew dokkaHtml
```

Licensing
=====

This software library is licensed und the terms of the MIT license, with the exemptions noted below.

You can find a copy of the license in the [LICENSE file](LICENSE).

Exemptions:
* miHoYo and its subsidiaries are exempt from the MIT licensing and may instead license any source code authored for the AnimeGameServer projects under the Zero-Clause BSD license.
