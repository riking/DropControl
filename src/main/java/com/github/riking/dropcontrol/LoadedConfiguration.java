package com.github.riking.dropcontrol;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.github.riking.dropcontrol.matcher.BaseMatcher;

public class LoadedConfiguration {
    private Action defaultAction;
    private List<BaseMatcher> globalMatchers;
    private Map<String, List<BaseMatcher>> worldMatchers;

    public Action checkItem(World world, ItemStack item) {
        return checkItem(world.getName(), item);
    }

    public Action checkItem(String world, ItemStack item) {

    }

    private Action tryCheck(List<BaseMatcher> matchers, ItemStack item) {
        for (BaseMatcher match : matchers) {

        }
    }
}
