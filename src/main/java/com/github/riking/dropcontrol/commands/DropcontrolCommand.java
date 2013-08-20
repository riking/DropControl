package com.github.riking.dropcontrol.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.github.riking.dropcontrol.DropControlMain;
import com.github.riking.dropcontrol.Util;
import com.google.common.collect.ImmutableList;

public class DropcontrolCommand implements TabExecutor {
    DropControlMain plugin;

    private Map<String, Subcommand> subcommands = new HashMap<String, Subcommand>();
    private Subcommand helpCmd;

    public DropcontrolCommand(DropControlMain plugin) {
        this.plugin = plugin;
        helpCmd = new SCHelp();
        subcommands.put("?", helpCmd);
        subcommands.put("help", helpCmd);
        subcommands.put("reload", new SCReload());
        subcommands.put("save", new SCSave());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Util.getPartialMatches(subcommands.keySet(), args[0]);
        } else if (args.length > 1) {
            String subcommand = args[0];
            Subcommand sc = subcommands.get(subcommand);
            if (sc != null) {
                if (!sender.hasPermission(sc.getPermission())) {
                    return ImmutableList.of();
                }
                return sc.onTabComplete(this, sender, args);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Subcommand sc;
        if (args.length >= 1) {
            String subcommand = args[0];
            sc = subcommands.get(subcommand);
        } else {
            sc = helpCmd;
        }
        if (sc != null) {
            if (!sender.hasPermission(sc.getPermission())) {
                sender.sendMessage(command.getPermissionMessage());
                return true;
            }

            return sc.onCommand(this, sender, args);
        } else {
            sender.sendMessage(args[0] + " is not a valid subcommand. Type " + ChatColor.AQUA + "/" + label + " ?" + ChatColor.RESET + "for help.");
            return true;
        }
    }
}
