package com.github.riking.dropcontrol;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class DropListener implements Listener {
    @NonNull private DropControlMain plugin;

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        if (item.isDead() || AIR.isSimilar(item.getItemStack())) {
            // the item was already removed by another plugin
            return;
        }
        Action action = plugin.config.checkItem(player.getWorld().getName(), item.getItemStack(), player);
        switch (action) {
            case ALLOW:
                return;
            case BLOCK:
                event.setCancelled(true);
                player.sendMessage(ChatColor.YELLOW + "[DropControl] You are not allowed to drop that!");
                break;
            case REMOVE:
                item.setItemStack(new ItemStack(Material.AIR));
                item.remove();
                // player.sendMessage(ChatColor.YELLOW + "[DropControl] You are not allowed to drop that!");
                break;
        }
    }
}
