package dev.ev1dent.kptokens;

import dev.ev1dent.kptokens.commands.CommandKPTokens;
import dev.ev1dent.kptokens.commands.CommandTokens;
import dev.ev1dent.kptokens.sql.MySQL;
import dev.ev1dent.kptokens.sql.PlayerHandler;
import dev.ev1dent.kptokens.sql.SQLGetter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class TokensMain extends JavaPlugin {

    public TokensMain plugin;
    public MySQL SQL;
    public SQLGetter data;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;
        this.data = new SQLGetter();
        connectDatabase();
        registerCommands();


    }

    public void registerCommands(){
        this.getCommand("tokens").setExecutor(new CommandTokens());
        this.getCommand("kptokens").setExecutor(new CommandKPTokens());
    }

    public void registerEvents(){
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
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
            data.createTable();
        }

    }
}
