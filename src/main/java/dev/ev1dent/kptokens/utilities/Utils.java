package dev.ev1dent.kptokens.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class Utils {


    public @NotNull Component formatMM(String s) {
        return MiniMessage.miniMessage().deserialize(s).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public Component kpMessage (String string){
        return formatMM(String.format("<green>%s", string));
    }

    public Component kpError(String string){
        return formatMM(String.format("<dark_red>Error: <red>%s", string));
    }

}
