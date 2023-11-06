package org.anime_game_servers.jnlua_engine;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;

@Data @AllArgsConstructor
public class StaticClassWrapper {
    @Nonnull private Class<?> staticClass;
}
