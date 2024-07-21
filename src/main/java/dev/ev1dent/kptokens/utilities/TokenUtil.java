package dev.ev1dent.kptokens.utilities;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class TokenUtil {

    Utils Utils = new Utils();

    public void addTokens(Player player, int tokens, CommandSender sender){
        // add {tokens} to {player}
        sender.sendMessage("Adding tokens " + tokens + " to " + player.getName());
    }

    public void removeTokens(Player player, int tokens, CommandSender sender) throws Exception {
        int currentTokens = getTokens();
        if(currentTokens < tokens){
            throw new Exception("Current token amount must be greater than tokens to remove.");
        }
        sender.sendMessage("Removing tokens " + tokens + " from " + player.getName());

    }

    public int getTokens(){
        return 0;
    }
}
