package com.github.riking.dropcontrol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.github.riking.dropcontrol.matcher.ItemMatcher;

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
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        return null;
    }
}
