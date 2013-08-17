package com.github.riking.dropcontrol.matcher;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TotalItemMatcher implements ItemMatcher {
    private ItemStack item;

    @Override
    public Object getSerializationObject() {
        return item;
    }

    @Override
    public boolean matches(ItemStack item2, Player player) {
        return item.isSimilar(item2);
    }

    @Override
    public String getSerializationKey() {
        return "totalitem";
    }
}
