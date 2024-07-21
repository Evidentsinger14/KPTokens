package dev.ev1dent.kptokens.utilities;

import dev.ev1dent.kptokens.TokensMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class Utils {
    TokensMain tokensMain = new TokensMain();

    public FileConfiguration Config() {
        return tokensMain.plugin.getConfig();
    }

    public @NotNull Component formatMM(String s) {
        return MiniMessage.miniMessage().deserialize(s).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

}
