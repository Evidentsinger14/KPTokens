package dev.ev1dent.kptokens.papi;

import dev.ev1dent.kptokens.sql.SQLGetter;
import org.bukkit.entity.Player;
import dev.ev1dent.kptokens.TokensMain;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class KPTokensExpansion extends PlaceholderExpansion {

    private TokensMain tokensMain(){
        return TokensMain.getPlugin(TokensMain.class);
    }

    SQLGetter data = new SQLGetter();

    @Override
    public @NotNull String getIdentifier() {
        return "kptokens";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ev1dent";
    }

    @Override
    public @NotNull String getVersion() {
        return tokensMain().getDescription().getVersion();
    }
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equals("amount")){
            return String.valueOf(data.getTokens(player.getUniqueId()));
        }
        return null;
    }
}
