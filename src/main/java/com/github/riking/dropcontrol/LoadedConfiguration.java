package com.github.riking.dropcontrol;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class LoadedConfiguration {
    private Action defaultAction;
    private List<BaseMatcher> globalMatchers;
    private Map<String, List<BaseMatcher>> worldMatchers;

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
}
