package dev.ev1dent.kptokens.sql;

import dev.ev1dent.kptokens.TokensMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler implements Listener {

    SQLGetter data = new SQLGetter();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        data.createPlayer(event.getPlayer());
    }
}
