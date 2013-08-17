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
        getServer().getPluginManager().registerEvents(new DropListener(this), this);
        loadConfiguration();
    }

    @Override
    public void onDisable() {

    }

    public void loadConfiguration() {
        configLoaded = false;
        config = null;

        Action defaultAction;
        List<BaseMatcher> globalMatchers = new ArrayList<BaseMatcher>();
        Map<String, List<BaseMatcher>> worldMatchers = new HashMap<String, List<BaseMatcher>>();

        ConfigurationSection root = getConfig();

        defaultAction = Action.valueOf(root.getString("default-action"));

        List<Map<?, ?>> global = root.getMapList("global");
        for (Map<?, ?> map : global) {
            @SuppressWarnings("unchecked")
            Map<String, Object> yamlmap = (Map<String, Object>) map;
            globalMatchers.add(new BaseMatcher(yamlmap));
        }

        ConfigurationSection perworld = root.getConfigurationSection("worlds");
        for (String world : perworld.getKeys(false)) {
            List<BaseMatcher> list = new ArrayList<BaseMatcher>();
            List<Map<?, ?>> rules = perworld.getMapList(world);
            for (Map<?, ?> map : rules) {
                @SuppressWarnings("unchecked")
                Map<String, Object> yamlmap = (Map<String, Object>) map;
                list.add(new BaseMatcher(yamlmap));
            }
            worldMatchers.put(world, list);
        }

        config = new LoadedConfiguration(defaultAction, globalMatchers, worldMatchers);
    }
}
