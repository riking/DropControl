package com.github.riking.dropcontrol;

import lombok.AllArgsConstructor;
import lombok.NonNull;

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

    @SuppressWarnings("unused")
    private static final ItemStack AIR = new ItemStack(Material.AIR);

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        if (item.isDead()) {
            // the item was already removed by another plugin
            return;
        }
        Action action = plugin.config.checkItem(player.getWorld().getName(), item.getItemStack(), player);
        String message = plugin.config.messages.get(action);
        if (!message.isEmpty()) {
            player.sendMessage(message);
        }
        switch (action) {
            case ALLOW:
                break;
            case BLOCK:
                event.setCancelled(true);
                break;
            case REMOVE:
                item.remove();
                // XXX Causes NPE in NMS-land
                // item.setItemStack(new ItemStack(Material.AIR));
                break;
        }
    }
}
