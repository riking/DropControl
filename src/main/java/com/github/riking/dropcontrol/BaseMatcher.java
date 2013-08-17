package com.github.riking.dropcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.riking.dropcontrol.matcher.ItemMatcher;
import com.github.riking.dropcontrol.matcher.LoreLineMatcher;
import com.github.riking.dropcontrol.matcher.PermissionExclusionMatcher;
import com.github.riking.dropcontrol.matcher.TotalItemMatcher;

public class BaseMatcher implements ConfigurationSerializable {
    @Getter private Action action;
    private List<ItemMatcher> matchers;

    /**
     *
     * @param action action to perform
     * @param matchers ItemMatchers to use
     */
    public BaseMatcher(Action action, List<ItemMatcher> matchers) {
        Validate.notNull(action, "Action may not be null");
        Validate.notEmpty(matchers, "You must use at least one matcher");
        this.action = action;
        this.matchers = matchers;
    }

    public BaseMatcher(Map<String, Object> data) {
        action = Action.valueOf((String) data.get("action"));
        matchers = new ArrayList<ItemMatcher>();
        if (data.containsKey("item")) {
            matchers.add(ItemStringInterpreter.valueOf((String) data.get("item")));
        }
        if (data.containsKey("loreline")) {
            matchers.add(new LoreLineMatcher((String) data.get("loreline")));
        }
        if (data.containsKey("totalitem")) {
            matchers.add(new TotalItemMatcher((ItemStack) data.get("totalitem")));
        }
        if (data.containsKey("exempt-permission")) {
            matchers.add(new PermissionExclusionMatcher((String) data.get("exempt-permission")));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", action.toString());
        for (ItemMatcher im : matchers) {
            map.put(im.getSerializationKey(), im.getSerializationObject());
        }
        return null;
    }

    public boolean appliesTo(ItemStack item, Player player) {
        for (ItemMatcher im : matchers) {
            if (!im.matches(item, player)) return false;
        }
        return true;
    }
}
