package com.github.riking.dropcontrol.matcher;

import lombok.AllArgsConstructor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BasicItemMatcher implements ItemMatcher {
    private Material material;
    private Short durability;
    private transient String dataString;

    /**
     * Intended logic: Outputs are preferred in this order.
     * <ol>
     * <li>"any"</li>
     * <li>material:datastring</li>
     * <li>material:dataid</li>
     * <li>material</li>
     * </ol>
     */
    @Override
    public String getSerializationObject() {
        if (material == null) {
            return "any";
        } else if (durability == null) {
            return material.toString();
        } else if (dataString != null) {
            return material.toString() + ":" + dataString;
        } else {
            return material.toString() + ":" + durability;
        }
    }

    @Override
    public String getSerializationKey() {
        return "item";
    }

    /**
     * Intended logic:
     * <ul>
     * <li>Return true if we're doing "any"</li>
     * <li>If not matching data, match only on material</li>
     * <li>Otherwise match both</li>
     * </ul>
     */
    @Override
    public boolean matches(ItemStack item, Player player) {
        if (material == null)
            return true;

        if (material != item.getType()) {
            return false;
        }
        if (durability != null) {
            return durability == item.getDurability();
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((durability == null) ? 0 : durability.hashCode());
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicItemMatcher other = (BasicItemMatcher) obj;
        if (durability == null) {
            if (other.durability != null)
                return false;
        } else if (!durability.equals(other.durability))
            return false;
        if (material != other.material)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getSerializationObject();
    }
}
