package com.github.riking.dropcontrol.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;

public class SCHelp implements Subcommand {

    @Override
    public List<String> onTabComplete(DropcontrolCommand baseCommand, CommandSender sender, String[] fullArgs) {
        return ImmutableList.of();
    }

    @Override
    public boolean onCommand(DropcontrolCommand commandObject, CommandSender sender, String[] fullArgs) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPermission() {
        return "dropcontrol.command.help";
    }

}
