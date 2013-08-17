package com.github.riking.dropcontrol.matcher;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemMatcher {
    public boolean matches(ItemStack item, Player player);

    public Object getSerializationObject();

    /**
     * The return value of this method must be the same across all instances
     * and recognized in the BaseMatcher class.
     *
     * @return
     */
    public String getSerializationKey();
}
