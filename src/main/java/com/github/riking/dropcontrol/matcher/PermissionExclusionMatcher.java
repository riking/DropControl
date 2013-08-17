package com.github.riking.dropcontrol.matcher;

import lombok.AllArgsConstructor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PermissionExclusionMatcher implements ItemMatcher {
    private String permission;

    public Object getSerializationObject() {
        return permission;
    }

    @Override
    public boolean matches(ItemStack item, Player player) {
        if (player.hasPermission(permission)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getSerializationKey() {
        return "exempt-permission";
    }
}
