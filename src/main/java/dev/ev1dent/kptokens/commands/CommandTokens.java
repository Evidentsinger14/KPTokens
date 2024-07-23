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

        if (args.length == 0) {
            Player player = (Player) sender;
            int tokens = data.getTokens(player.getUniqueId());
            sender.sendMessage(Utils.formatMM("<white>Tokens: <green>" + tokens));
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(Utils.kpError("Please specify a player name, and Token Amount."));
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(Utils.kpError("Player not found."));
            return true;
        }

        String type = args[0].toLowerCase();
        int tokens = Integer.parseInt(args[2]);

        switch (type) {
            case "give":
                if (!sender.hasPermission("kptokens.tokens.give")) return true;
                addTokens(tokens, sender, player);
                break;

            case "remove":
                if (!sender.hasPermission("kptokens.tokens.remove")) return true;
                removeTokens(tokens, sender, player);
                break;

            case "set":
                if (!sender.hasPermission("kptokens.tokens.set")) return true;
                setTokens(tokens, sender, player);
                break;

            default:
                sender.sendMessage(Utils.kpError("Unknown Command."));
                break;

        }
        return true;
}

    private void removeTokens(int tokens, CommandSender sender, Player player){
        int newAmount = data.getTokens(player.getUniqueId()) - tokens;
        if(newAmount < 0) return;

        try {
            data.removeTokens(player.getUniqueId(), tokens);
        } catch (Exception e) {
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
        sender.sendMessage(Utils.kpMessage("Removed " + tokens + " token(s) from " + player.getName()));
    }

    private void addTokens(int tokens, CommandSender sender, Player player){
        try{
            data.addTokens(player.getUniqueId(), tokens);
        } catch (Exception e){
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
        sender.sendMessage(Utils.kpMessage("Added " + tokens + " token(s) to " + player.getName()));
    }

    private void setTokens(int tokens, CommandSender sender, Player player){
        try{
            data.setTokens(player.getUniqueId(), tokens);
        } catch (Exception e){
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
        sender.sendMessage(Utils.kpMessage("Set " + player.getName() + "'s tokens to " + tokens ));
    }

}
