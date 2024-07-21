package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.utilities.TokenUtil;
import dev.ev1dent.kptokens.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTokens implements CommandExecutor {

    TokenUtil tokenUtil = new TokenUtil();
    Utils Utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 0) {
            if(sender == null){
                sender.sendMessage("You must be a player to use this command!");
            }
            sender.sendMessage(Utils.formatMM("<white>Tokens: <green>" + tokenUtil.getTokens()));
            return true;
        }
        if(!sender.hasPermission("kptokens.tokens." + args[0])){
            sender.sendMessage(Utils.kpError("You do not have permission to use that subcommand."));
        }
        processTokens(sender, args);

        return false;
    }

    private void processTokens(CommandSender sender, String[] args){
        String type = args[0].toLowerCase();
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            sender.sendMessage(Utils.kpError("Player not found."));
            return;
        }
        if(type.equals("give")){
            if(!sender.hasPermission("kptokens.tokens.give")) return;
            try{
                int tokens = Integer.parseInt(args[2]);
                tokenUtil.addTokens(player, tokens);
            } catch (Exception e){
                sender.sendMessage(Utils.kpError(e.getMessage()));
            }
        } else if(type.equals("remove")){
            if(!sender.hasPermission("kptokens.tokens.remove")) return;
            try{
                int tokens = Integer.parseInt(args[2]);
                tokenUtil.removeTokens(player, tokens);
            } catch (Exception e){
                sender.sendMessage(Utils.kpError(e.getMessage()));
            }
        }
    }


}
