package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.TokensMain;
import dev.ev1dent.kptokens.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandKPTokens implements CommandExecutor {

    private TokensMain tokensMain(){
        return TokensMain.getPlugin(TokensMain.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        Utils Utils = new Utils();
        if (!sender.hasPermission("kptokens.kptokens")){
            sender.sendMessage(Utils.kpError("You don't have permission to use this command!"));
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0]){
            case "version":
                sender.sendMessage(Utils.kpMessage("            <green><bold>KPTokens            "));
                sender.sendMessage(Utils.kpMessage("<strikethrough>                                       "));
                sender.sendMessage(Utils.kpMessage("<dark_green>This server is running <green><underlined>KPTokens <yellow>v" + tokensMain().plugin.getDescription().getVersion()));
                sender.sendMessage(Utils.kpMessage("<dark_green>- <green>Bukkit Version: " + Bukkit.getVersion()));
                break;

            case "reload":
                tokensMain().reloadConfig();
                sender.sendMessage(Utils.kpMessage("Reloaded configuration"));
                break;

            default:
                sender.sendMessage(Utils.kpMessage("Invalid command"));
                sender.sendMessage(Utils.kpMessage("Usage"));
                sender.sendMessage(Utils.kpMessage("/kptokens version - displays version information"));
                sender.sendMessage(Utils.kpMessage("/kptokens reload - reloads configuration"));
                break;
        }
        return false;
    }
}
