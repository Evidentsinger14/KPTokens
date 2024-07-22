package dev.ev1dent.kptokens;

import dev.ev1dent.kptokens.commands.CommandKPTokens;
import dev.ev1dent.kptokens.commands.CommandTokens;
import dev.ev1dent.kptokens.papi.KPTokensExpansion;
import dev.ev1dent.kptokens.sql.MySQL;
import dev.ev1dent.kptokens.sql.PlayerHandler;
import dev.ev1dent.kptokens.sql.SQLGetter;
import dev.ev1dent.kptokens.utilities.TabCompletion;
import org.bukkit.Bukkit;
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
        registerEvents();
        initializeDependencies();
    }

    public void registerCommands(){
        this.getCommand("tokens").setExecutor(new CommandTokens());
        this.getCommand("kptokens").setExecutor(new CommandKPTokens());
        addTabCompletion();
    }

    public void registerEvents(){
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
    }

    public void initializeDependencies(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KPTokensExpansion().register();
        }
    }

    public void addTabCompletion(){
      this.getCommand("tokens").setTabCompleter(new TabCompletion());
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
