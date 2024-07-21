package dev.ev1dent.kptokens;

import dev.ev1dent.kptokens.commands.CommandTokens;
import org.bukkit.plugin.java.JavaPlugin;

public final class TokensMain extends JavaPlugin {

    public TokensMain plugin;

    @Override
    public void onEnable() {
        plugin = this;
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void registerCommands(){
        this.getCommand("tokens").setExecutor(new CommandTokens());
    }
}
