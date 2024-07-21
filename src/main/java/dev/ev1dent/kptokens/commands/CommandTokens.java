package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.commands.utilities.KPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTokens implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        KPPlayer kpPlayer = (KPPlayer) sender;
        if(args.length == 0) {
            // Return token amount to player. need to do mysql bullshit for this though.
        }
        if(!sender.hasPermission(String.format("kptokens.tokens.%s", args[0]))) return true;
        switch (args[0]) {
            case "give":
                try{
                    int tokens = Integer.parseInt(args[1]);
                    kpPlayer.addTokens(tokens);
                } catch (Exception e){
                    kpPlayer.sendKPMessage(e.getMessage());
                }
                break;
            case "remove":
                try{
                    int tokens = Integer.parseInt(args[1]);
                    kpPlayer.removeTokens(tokens);
                } catch (Exception e){
                    kpPlayer.sendKPMessage(e.getMessage());
                }
                break;
        }
        return false;
    }
}
