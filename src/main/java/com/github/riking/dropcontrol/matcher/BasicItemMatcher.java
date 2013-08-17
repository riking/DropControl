package com.github.riking.dropcontrol.matcher;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@EqualsAndHashCode
public class BasicItemMatcher implements ItemMatcher {
    private Material material;
    private Short durability;
    private String dataString;

    @Override
    public Object getSerializationObject() {
        if (durability == null) {
            return material.toString();
        } else if (dataString != null) {
            return material.toString() + ":" + dataString;
        } else {
            return material.toString() + ":" + durability;
        }
    }

    @Override
    public boolean matches(ItemStack item) {
        if (material != item.getType()) {
            return false;
        }
        if (durability != null) {
            return durability == item.getDurability();
        }
        return true;
    }
}
