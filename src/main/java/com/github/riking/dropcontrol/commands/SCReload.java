package com.github.riking.dropcontrol.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;

public class SCReload implements Subcommand {
    @Override
    public boolean onCommand(DropcontrolCommand commandObject, CommandSender sender, String[] fullArgs) {
        commandObject.plugin.reloadConfig();
        return true;
    }

    @Override
    public List<String> onTabComplete(DropcontrolCommand commandObject, CommandSender sender, String[] fullArgs) {
        return ImmutableList.of();
    }

    @Override
    public String getPermission() {
        return "dropcontrol.command.reload";
    }
}
