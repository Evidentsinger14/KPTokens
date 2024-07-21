package dev.ev1dent.kptokens.commands.utilities;

import dev.ev1dent.kptokens.TokensMain;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {
    TokensMain tokensMain = new TokensMain();

    public FileConfiguration Config() {
        return tokensMain.plugin.getConfig();
    }

}
