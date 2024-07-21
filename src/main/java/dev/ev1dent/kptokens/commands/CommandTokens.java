package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.utilities.KPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTokens implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) sender.sendMessage("This command can only be used by Players");

        KPPlayer kpPlayer = (KPPlayer) sender;

        if(args.length == 0) kpPlayer.sendKPMessage("<white>Tokens: <green>" + kpPlayer.getTokens());

        switch (args[0]) {
            case "give":
                if(!sender.hasPermission(String.format("kptokens.tokens.%s", args[0]))){
                    kpPlayer.sendKPError("You don't have permission to use this command.");
                }
                try{
                    int tokens = Integer.parseInt(args[1]);
                    kpPlayer.addTokens(tokens);
                } catch (Exception e){
                    kpPlayer.sendKPError(e.getMessage());
                }
                break;

            case "remove":
                if(!sender.hasPermission(String.format("kptokens.tokens.%s", args[0]))){
                    kpPlayer.sendKPError("You don't have permission to use this command.");
                }
                try{
                    int tokens = Integer.parseInt(args[1]);
                    kpPlayer.removeTokens(tokens);
                } catch (Exception e){
                    kpPlayer.sendKPMessage(e.getMessage());
                }
                break;
            default:
                kpPlayer.sendKPError("Invalid command.");
                break;
        }
        return false;
    }
}
