package com.github.riking.dropcontrol.matcher;

import org.bukkit.inventory.ItemStack;

public interface ItemMatcher {
    public Object getSerializationObject();
    public boolean matches(ItemStack item);
}
