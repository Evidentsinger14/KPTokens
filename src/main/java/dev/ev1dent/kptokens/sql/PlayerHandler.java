package dev.ev1dent.kptokens.sql;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerHandler implements Listener {

    SQLGetter data = new SQLGetter();
    MySQL sql = new MySQL();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException, ClassNotFoundException {
        if(!sql.isConnected()) sql.connect();
        data.createPlayer(event.getPlayer());
    }
}
