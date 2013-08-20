package com.github.riking.dropcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class LoadedConfiguration {
    private Action defaultAction;
    private List<BaseMatcher> globalMatchers;
    private Map<String, List<BaseMatcher>> worldMatchers;
    public Map<Action, String> messages;

    public Action checkItem(String world, ItemStack item, Player player) {
        Action act;
        act = tryCheck(worldMatchers.get(world), item, player);
        if (act != null) return act;
        act = tryCheck(globalMatchers, item, player);
        if (act != null) return act;
        return defaultAction;
    }

    /**
     * Returns null if no matching rule was found.
     *
     * @param matchers list of matching rules
     * @param item item to check
     * @return action if rule found, else null
     */
    private Action tryCheck(List<BaseMatcher> matchers, ItemStack item, Player player) {
        for (BaseMatcher match : matchers) {
            if (match.appliesTo(item, player)) {
                return match.getAction();
            }
        }
        return null;
    }

    public ConfigurationSection saveTo(ConfigurationSection config) {
        ConfigurationSection section;

        config.set("default-action", defaultAction.toString());

        section = getOrCreateSection(config, "messages");
        section.set("ALLOW", messages.get(Action.ALLOW));
        section.set("BLOCK", messages.get(Action.BLOCK));
        section.set("REMOVE", messages.get(Action.REMOVE));

        config.set("global", saveMatchers(globalMatchers));

        section = getOrCreateSection(config, "worlds");
        for (String world : worldMatchers.keySet()) {
            section.set(world, saveMatchers(worldMatchers.get(world)));
        }
        return config;
    }

    private ConfigurationSection getOrCreateSection(ConfigurationSection base, String name) {
        ConfigurationSection sec = base.getConfigurationSection(name);
        if (sec == null) {
            sec = base.createSection(name);
        }
        return sec;
    }

    private List<Map<?, ?>> saveMatchers(List<BaseMatcher> matchers) {
        List<Map<?, ?>> maplist = new ArrayList<Map<?, ?>>(matchers.size());
        for (BaseMatcher match : matchers) {
            maplist.add(match.serialize());
        }
        return maplist;
    }
}
