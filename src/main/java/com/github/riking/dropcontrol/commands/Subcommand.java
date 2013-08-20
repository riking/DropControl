package com.github.riking.dropcontrol.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    public List<String> onTabComplete(DropcontrolCommand commandObject, CommandSender sender, String[] fullArgs);
    public boolean onCommand(DropcontrolCommand commandObject, CommandSender sender, String[] fullArgs);
    public String getPermission();
}
