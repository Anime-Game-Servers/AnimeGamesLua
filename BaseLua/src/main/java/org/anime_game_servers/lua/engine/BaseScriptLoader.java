package org.anime_game_servers.lua.engine;

import java.io.InputStream;
import java.nio.file.Path;

public interface BaseScriptLoader {
    InputStream openScript(Path scriptPath);
    Path getScriptPath(String scriptName);
}
