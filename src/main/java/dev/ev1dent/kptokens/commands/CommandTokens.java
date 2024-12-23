package dev.ev1dent.kptokens.commands;

import dev.ev1dent.kptokens.TokensMain;
import dev.ev1dent.kptokens.sql.SqlStorage;
import dev.ev1dent.kptokens.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommandTokens implements CommandExecutor {

    private TokensMain tokensMain() {
        return TokensMain.getPlugin(TokensMain.class);
    }

    SqlStorage data = tokensMain().sqlStorage;
    Utils Utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            if(!(sender instanceof Player player)) return false;

            data.getTokensAsync(player.getUniqueId())
                    .thenAcceptOnMain(tokens -> sender.sendMessage(Utils.formatMM("<white>Tokens: <green>" + tokens)));
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

        int tokens;
        try{
            tokens = Integer.parseInt(args[2]);
        } catch (Exception e){
            sender.sendMessage(Utils.kpError("Invalid token amount"));
            return true;
        }

        switch (type) {
            case "give":
                if (!sender.hasPermission("kptokens.tokens.give")) return true;
                addTokens(tokens, sender, player, args);
                break;

            case "remove":
                if (!sender.hasPermission("kptokens.tokens.remove")) return true;
                removeTokens(tokens, sender, player, args);
                break;

            case "set":
                if (!sender.hasPermission("kptokens.tokens.set")) return true;
                setTokens(tokens, sender, player, args);
                break;

            default:
                sender.sendMessage(Utils.kpError("Unknown Command."));
                break;

        }
        return true;
}

    private void removeTokens(int tokens, CommandSender sender, Player player, String[] args){
        UUID uuid = player.getUniqueId();
        try {
            data.getTokensAsync(uuid).thenCompose(currentTokens -> {
                int newAmount = currentTokens - tokens;
                if (newAmount < 0) return new CompletableFuture<>();

                return data.removeTokensAsync(uuid, tokens);
            }).thenRunOnMain(() -> {
                String message = "Removed " + tokens + " token(s) from %s";
                sender.sendMessage(Utils.kpMessage(String.format(message, player.getName())));

                if (isSilenced(args)) {
                    player.sendMessage(Utils.kpMessage(String.format(message, "your balance.")));
                }
            });
        } catch (Exception e) {
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
    }

    private void addTokens(int tokens, CommandSender sender, Player player, String[] args){
        try {
            data.addTokensAsync(player.getUniqueId(), tokens).thenRunOnMain(() -> {
                String message = "Added " + tokens + " token(s) to %s";
                sender.sendMessage(Utils.kpMessage(String.format(message, player.getName())));

                if (isSilenced(args)) {
                    player.sendMessage(Utils.kpMessage(String.format(message, "your balance.")));
                }
            });
        } catch (Exception e) {
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
    }

    private void setTokens(int tokens, CommandSender sender, Player player, String[] args){
        try {
            data.setTokensAsync(player.getUniqueId(), tokens).thenRunOnMain(() -> {
                String message = "Set %s token balance to " + tokens;
                sender.sendMessage(Utils.kpMessage(String.format(message, player.getName() + "'s")));

                if (isSilenced(args)) {
                    player.sendMessage(Utils.kpMessage(String.format(message, "your")));
                }
            });
        } catch (Exception e) {
            sender.sendMessage(Utils.kpError(e.getMessage()));
        }
    }

    private boolean isSilenced(String[] args){
        boolean silenced = false;
        try {
            String silent = args[3].toLowerCase();
            if(silent.equals("-s")){
                silenced = true;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
        return !silenced;
    }

}
