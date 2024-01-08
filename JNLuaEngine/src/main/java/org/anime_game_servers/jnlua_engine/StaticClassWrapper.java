package org.anime_game_servers.jnlua_engine;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;

@Data
public class StaticClassWrapper {
    @Nonnull private Class<?> staticClass;

    public StaticClassWrapper(@Nonnull Class<?> staticClass){
        this.staticClass = staticClass;
    }
}
