package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.sql.SQLGetter;
import dev.ev1dent.kptokens.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTokens implements CommandExecutor {

    SQLGetter data = new SQLGetter();
    Utils Utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 0) {
            Player player = (Player) sender;
            int tokens = data.getTokens(player.getUniqueId());
            sender.sendMessage(Utils.formatMM("<white>Tokens: <green>" + tokens));
            return true;
        }

        if(args.length == 1){
            sender.sendMessage(Utils.kpError("Please specify a player name, and Token Amount."));
            return true;
        }
        processTokens(sender, args);

        return true;
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
                int newAmount = data.getTokens(player.getUniqueId()) + tokens;

                sender.sendMessage(Utils.kpMessage("Added " + tokens + " tokens to " + player.getName() + "'s Balance."));
                sender.sendMessage(Utils.kpMessage("New Token Balance: <white>" + newAmount));
                data.addTokens(player.getUniqueId(), tokens);

                if(!sender.getName().equals(player.getName())){
                    player.sendMessage(Utils.kpMessage("<white>" + tokens + "</white> tokens have been added to your balance."));
                }

            } catch (Exception e){
                sender.sendMessage(Utils.kpError(e.getMessage()));
            }
        } else if(type.equals("remove")){
            if(!sender.hasPermission("kptokens.tokens.remove")) return;
            try{

                int tokens = Integer.parseInt(args[2]);
                int newAmount = data.getTokens(player.getUniqueId()) - tokens;
                if(newAmount < 0){
                    throw new Exception("Token amount cannot be less than zero.");
                }
                sender.sendMessage(Utils.kpMessage("Removed " + tokens + " tokens from " + player.getName() + "'s Balance."));
                sender.sendMessage(Utils.kpMessage("New Token Balance: <white>" + newAmount));
                data.removeTokens(player.getUniqueId(), tokens);
                if(!sender.getName().equals(player.getName())){
                    player.sendMessage(Utils.kpMessage("<white>" + tokens + "</white> tokens have been removed from your balance."));
                }
            } catch (Exception e){
                sender.sendMessage(Utils.kpError(e.getMessage()));
            }
        }
        sender.sendMessage(Utils.kpError("Unknown Command."));
    }


}
