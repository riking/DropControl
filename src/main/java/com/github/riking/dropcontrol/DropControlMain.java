package com.github.riking.dropcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class DropControlMain extends JavaPlugin {
    public LoadedConfiguration config;
    public boolean configLoaded;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DropListener(this), this);
        loadConfiguration();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        loadConfiguration();
    }

    @Override
    public void saveConfig() {
        config.saveTo(getConfig());
        super.saveConfig();
    }

    public void loadConfiguration() {
        configLoaded = false;
        config = null;

        Action defaultAction;
        List<BaseMatcher> globalMatchers = new ArrayList<BaseMatcher>();
        Map<String, List<BaseMatcher>> worldMatchers = new HashMap<String, List<BaseMatcher>>();
        Map<Action, String> messages = new HashMap<Action, String>();

        ConfigurationSection root = getConfig();

        // defaultAction
        defaultAction = Action.valueOf(root.getString("default-action"));

        // globalMatchers
        List<Map<?, ?>> global = root.getMapList("global");
        for (Map<?, ?> map : global) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> yamlmap = (Map<String, Object>) map;
                globalMatchers.add(new BaseMatcher(yamlmap));
            } catch (Exception e) {
                new IllegalArgumentException("Malformed configuration entry", e).printStackTrace();
            }
        }

        // worldMatchers
        ConfigurationSection perworld = root.getConfigurationSection("worlds");
        for (String world : perworld.getKeys(false)) {
            List<BaseMatcher> list = new ArrayList<BaseMatcher>();
            List<Map<?, ?>> rules = perworld.getMapList(world);
            for (Map<?, ?> map : rules) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> yamlmap = (Map<String, Object>) map;
                    list.add(new BaseMatcher(yamlmap));
                } catch (Exception e) {
                    new IllegalArgumentException("Malformed configuration entry", e).printStackTrace();
                }
            }
            worldMatchers.put(world, list);
        }

        // messages
        ConfigurationSection msgs = root.getConfigurationSection("messages");
        messages.put(Action.ALLOW, ChatColor.translateAlternateColorCodes('&', msgs.getString("ALLOW")));
        messages.put(Action.BLOCK, ChatColor.translateAlternateColorCodes('&', msgs.getString("BLOCK")));
        messages.put(Action.REMOVE, ChatColor.translateAlternateColorCodes('&', msgs.getString("REMOVE")));

        config = new LoadedConfiguration(defaultAction, globalMatchers, worldMatchers, messages);
    }
}
