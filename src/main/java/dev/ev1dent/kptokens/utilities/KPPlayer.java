package dev.ev1dent.kptokens.utilities;

import org.bukkit.entity.Player;

public abstract class KPPlayer implements Player {
    Player player;
    Utils Utils = new Utils();

    public void sendKPMessage(String string){
        player.sendMessage(Utils.formatMM("<green>" + string));
    }

    public void sendKPError(String string){
        player.sendMessage(Utils.formatMM("<red>Error: " + string + "<light_red>"));
    }

    public void addTokens(int tokens){

    }

    public void removeTokens(int tokens) throws Exception {
        int currentTokens = getTokens();
        if(currentTokens < tokens){
            throw new Exception("Current token amount must be greater than tokens to remove.");
        }
    }

    public int getTokens(){
        return 0;
    }



}
