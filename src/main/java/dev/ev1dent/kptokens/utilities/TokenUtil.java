package dev.ev1dent.kptokens.utilities;

import org.bukkit.entity.Player;
public class TokenUtil {

    public void addTokens(Player player, int tokens){

    }

    public void removeTokens(Player player, int tokens) throws Exception {
        int currentTokens = getTokens();
        if(currentTokens < tokens){
            throw new Exception("Current token amount must be greater than tokens to remove.");
        }
    }

    public int getTokens(){
        return 0;
    }
}
