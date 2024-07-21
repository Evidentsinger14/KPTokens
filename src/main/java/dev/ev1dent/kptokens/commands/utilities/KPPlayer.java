package dev.ev1dent.kptokens.commands.utilities;

import org.bukkit.entity.Player;

public abstract class KPPlayer implements Player {
    Player player;

    public void sendKPMessage(String string){
        player.sendMessage(string);
    }

    public void addTokens(int tokens){

    }

    public void removeTokens(int tokens) throws Exception {
        int currentTokens = 0;
        if(currentTokens < tokens){
            throw new Exception("Current token amount must be greater than tokens to remove.");
        }
    }

    public int getTokens(){
        return 0;
    }

}
