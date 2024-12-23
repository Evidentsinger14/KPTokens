package dev.ev1dent.kptokens.sql;

import dev.ev1dent.kptokens.TokensMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerHandler implements Listener {

    private TokensMain tokensMain() {
        return TokensMain.getPlugin(TokensMain.class);
    }

    SqlStorage data = tokensMain().sqlStorage;

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        data.createPlayer(event.getUniqueId());
    }
}
