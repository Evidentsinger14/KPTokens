package dev.ev1dent.kptokens;

import dev.ev1dent.kptokens.commands.CommandKPTokens;
import dev.ev1dent.kptokens.commands.CommandTokens;
import dev.ev1dent.kptokens.sql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class TokensMain extends JavaPlugin {

    public TokensMain plugin;
    public MySQL SQL;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;
        connectDatabase();
        registerCommands();


    }

    public void registerCommands(){
        this.getCommand("tokens").setExecutor(new CommandTokens());
        this.getCommand("kptokens").setExecutor(new CommandKPTokens());
    }

    public void connectDatabase(){
        this.SQL = new MySQL();
        try{
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().severe(e.getMessage());
        }

        if(SQL.isConnected()){
            getLogger().info("Connected to database!");
        }

    }
}
