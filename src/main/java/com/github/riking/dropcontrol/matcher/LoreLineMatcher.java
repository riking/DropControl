package com.github.riking.dropcontrol.matcher;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public class LoreLineMatcher implements ItemMatcher {
    private String loreLine;

    public Object getSerializationObject() {
        return loreLine;
    }

    @Override
    public boolean matches(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        List<String> lore = meta.getLore();
        for (String s : lore) {
            if (loreLine.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
