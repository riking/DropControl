package com.github.riking.dropcontrol;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.riking.dropcontrol.matcher.BasicItemMatcher;

@RunWith(Parameterized.class)
public class StringInterpreterTestValid {
    private String testString;
    private BasicItemMatcher expected;

    public StringInterpreterTestValid(String s, BasicItemMatcher m) {
        testString = s;
        expected = m;
    }

    @Parameters
    public static Collection testCases() {
        return Arrays.asList(new Object[][] {
                // General handling tests
                {"any", new BasicItemMatcher(null, null, null)}, // Test any
                {"35", new BasicItemMatcher(Material.WOOL, null, null)}, // Test id alone
                {"35:4", new BasicItemMatcher(Material.WOOL, (short) 4, null)}, // Test id:data
                {"35:YELLOW", new BasicItemMatcher(Material.WOOL, (short) 4, null)}, // Test id:string
                {"WOOL:4", new BasicItemMatcher(Material.WOOL, (short) 4, null)}, // Test material:data
                {"WOOL:YELLOW", new BasicItemMatcher(Material.WOOL, (short) 4, null)}, // Test material:string
                {"35:YelLoW", new BasicItemMatcher(Material.WOOL, (short) 4, null)}, // Casing
                // Test every entry in materialData
                {"coal:charcoal", new BasicItemMatcher(Material.COAL, (short) 1, null)},
                {"leaves:birch", new BasicItemMatcher(Material.LEAVES, (short) 2, null)},
                {"long_grass:dead", new BasicItemMatcher(Material.LONG_GRASS, (short) GrassSpecies.DEAD.getData(), null)},
                {"sandstone:glyphed", new BasicItemMatcher(Material.SANDSTONE, (short) 1, null)},
                {"monster_egg:skeleton", new BasicItemMatcher(Material.MONSTER_EGG, (short) 51, null)},
                {"wood:redwood", new BasicItemMatcher(Material.WOOD, (short) 1, null)},
                {"wood_step:jungle", new BasicItemMatcher(Material.WOOD_STEP, (short) 3, null)},
                {"wool:light_blue", new BasicItemMatcher(Material.WOOL, (short) 3, null)},
                {"step:brick", new BasicItemMatcher(Material.STEP, (short) 4, null)},
                {"smooth_brick:cobblestone", new BasicItemMatcher(Material.SMOOTH_BRICK, (short) 2, null)},
                {"monster_eggs:cobblestone", new BasicItemMatcher(Material.MONSTER_EGGS, (short) 1, null)},
                {"ink_sack:yellow", new BasicItemMatcher(Material.INK_SACK, (short) 11, null)},
                {"skull_item:player", new BasicItemMatcher(Material.SKULL_ITEM, (short) 3, null)},
                {"cobble_wall:mossy_cobblestone", new BasicItemMatcher(Material.COBBLE_WALL, (short) 1, null)},
                {"anvil:slightly_damaged", new BasicItemMatcher(Material.ANVIL, (short) 1, null)},
                {"quartz_block:pillar", new BasicItemMatcher(Material.QUARTZ_BLOCK, (short) 2, null)},
                {"CARPET:YELLOW", new BasicItemMatcher(Material.CARPET, (short) 4, null)},
                {"stained_clay:YELLOW", new BasicItemMatcher(Material.STAINED_CLAY, (short) 4, null)},
        });
    }

    @Test
    public void testInterpretation() {
        BasicItemMatcher result = ItemStringInterpreter.valueOf(testString);
        Validate.isTrue(expected.equals(result), "Expected: " + expected.toString() + " Got: " + result.toString());
    }
}
