package dev.ev1dent.kptokens.utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompletion implements TabCompleter {
    private final String[] inputArgs = { "give", "remove", "set"};
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("tokens")) {
            switch (args.length) {
                case 1: {
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(inputArgs), completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2: {
                    return null;
                }
            }
        }
        return null;
    }
}