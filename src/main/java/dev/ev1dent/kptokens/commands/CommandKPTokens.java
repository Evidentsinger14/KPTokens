package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.TokensMain;
import dev.ev1dent.kptokens.utilities.KPPlayer;
import dev.ev1dent.kptokens.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandKPTokens implements CommandExecutor {
    Utils Utils = new Utils();
    TokensMain tokensMain = new TokensMain();

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        KPPlayer kpPlayer = (KPPlayer) sender;
        if (!sender.hasPermission("kptokens.kptokens")){
            kpPlayer.sendKPMessage("You don't have permission to use this command!");
        }

        switch (args[0]){
            case null:

                break;

            case "version":
                kpPlayer.sendKPMessage("            <light_green><bold>KPTokens            ");
                kpPlayer.sendKPMessage("<strikethrough>                                       ");
                kpPlayer.sendKPMessage("<green>This server is running <light_green><underlined>KPTokens <yellow>v" + tokensMain.plugin.getDescription().getVersion());
                kpPlayer.sendKPMessage("<green>- <light_green>Bukkit Version: " + Bukkit.getVersion());
                break;

            default:
                kpPlayer.sendKPMessage("Invalid command");
                break;
        }
        return false;
    }
}
