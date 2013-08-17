package com.github.riking.dropcontrol;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.CoalType;
import org.bukkit.DyeColor;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.SkullType;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.EntityType;
import org.bukkit.material.Coal;
import org.bukkit.material.Leaves;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.Sandstone;
import org.bukkit.material.SmoothBrick;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Step;
import org.bukkit.material.TexturedMaterial;
import org.bukkit.material.Tree;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.Wool;

import com.github.riking.dropcontrol.matcher.BasicItemMatcher;

@SuppressWarnings("rawtypes")
public class ItemStringInterpreter {
    private static Map<Class<? extends MaterialData>, Class<? extends Enum>> materialData;
    private static Map<Material, StringInterpreter> workarounds;

    static {
        // Block-only data classes are commented out, as we don't actually need them.
        //materialData.put(Bed.class, BlockFace.class);
        materialData.put(Coal.class, CoalType.class);
        //materialData.put(Chest.class, BlockFace.class);
        //materialData.put(Crops.class, CropState.class);
        //materialData.put(Dispenser.class, BlockFace.class);
        //materialData.put(EnderChest.class, BlockFace.class);
        //materialData.put(Furnace.class, BlockFace.class);
        materialData.put(Leaves.class, TreeSpecies.class);
        materialData.put(LongGrass.class, GrassSpecies.class);
        //materialData.put(NetherWarts.class, NetherWartsState.class);
        //materialData.put(Pumpkin.class, BlockFace.class);
        materialData.put(Sandstone.class, SandstoneType.class);
        //materialData.put(Skull.class, BlockFace.class);
        materialData.put(SpawnEgg.class, EntityType.class);
        materialData.put(Tree.class, TreeSpecies.class);
        //materialData.put(TripwireHook.class, BlockFace.class);
        materialData.put(WoodenStep.class, TreeSpecies.class);
        materialData.put(Wool.class, DyeColor.class);

        workarounds.put(Material.STEP, new TexturedMaterialInterpreter(new Step()));
        workarounds.put(Material.SMOOTH_BRICK, new TexturedMaterialInterpreter(new SmoothBrick()));
        workarounds.put(Material.MONSTER_EGGS, new TexturedMaterialInterpreter(new MonsterEggs()));
        // Workaround: Dye.class does not accept DyeColor in constructor
        workarounds.put(Material.INK_SACK, new StringInterpreter() {
            @Override
            public byte interpret(String s) {
                return DyeColor.valueOf(s).getDyeData();
            }
        });
        // Workaround: No MaterialData class for SKULL_ITEM
        workarounds.put(Material.SKULL_ITEM, new EnumOrdinalMaterialInterpreter(SkullType.values()));
        // Workaround: No MaterialData class for COBBLE_WALL
        workarounds.put(Material.COBBLE_WALL, new EnumOrdinalMaterialInterpreter(TEMP_WallType.values()));
        // Workaround: No MaterialData class for ANVIL
        workarounds.put(Material.ANVIL, new EnumOrdinalMaterialInterpreter(TEMP_AnvilDamage.values()));
        // Workaround: No MaterialData class for QUARTZ_BLOCK
        workarounds.put(Material.QUARTZ_BLOCK, new EnumOrdinalMaterialInterpreter(TEMP_QuartzType.values()));
        // These both should use Wool.class but don't
        StringInterpreter woolHandler = new StringInterpreter() {
            @Override
            public byte interpret(String s) {
                return DyeColor.valueOf(s).getWoolData();
            }
        };
        workarounds.put(Material.STAINED_CLAY, woolHandler);
        workarounds.put(Material.CARPET, woolHandler);
    }

    public static BasicItemMatcher valueOf(String itemString) throws IllegalArgumentException {
        String[] split = itemString.split(":");
        Validate.isTrue(split.length <= 2, "Unable to parse item string - too many colons (maximum 1). Please correct the format and reload the config. Input: " + itemString);
        Material mat = getMaterial(split[0]);
        Validate.notNull(mat, "Unable to parse item string - unrecognized material. Please correct the format and reload the config. Input: " + itemString);
        if (split.length == 1) {
            return new BasicItemMatcher(mat, null, null);
        }
        String dataString = split[1].toUpperCase();
        short data;
        try {
            data = Short.parseShort(dataString);
            return new BasicItemMatcher(mat, data, null); // the datastring is not passed if it was just a number
        } catch (NumberFormatException ignored) {
        }
        if (materialData.containsKey(mat.getData())) {
            Class<? extends MaterialData> matClass = mat.getData();
            Class<? extends Enum> enumClass = materialData.get(mat.getData());
            Enum enumValue;
            try {
                enumValue = (Enum) enumClass.getMethod("valueOf", String.class).invoke(null, dataString);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unable to parse item string - " + dataString + " is not a valid member of " + enumClass.getSimpleName());
            } catch (Exception rethrow) {
                throw new RuntimeException("Unexpected exception when parsing item string", rethrow);
            }
            MaterialData matData;
            try {
                matData = matClass.getConstructor(enumClass).newInstance(enumValue);
            } catch (Exception rethrow) {
                throw new RuntimeException("Unexpected exception when parsing item string", rethrow);
            }
            data = (short) matData.getData();
            return new BasicItemMatcher(mat, data, dataString);
        }
        if (workarounds.containsKey(mat)) {
            StringInterpreter interp = workarounds.get(mat);
            data = interp.interpret(dataString);
            return new BasicItemMatcher(mat, data, dataString);
        }
        throw new IllegalArgumentException("Unable to parse item string - I don't know how to parse a word-data for " + mat);
    }

    private static Material getMaterial(String s) {
        Material m = null;
        try {
            m = Material.matchMaterial(s);
        } catch (Exception e) {
        }
        if (m != null) return m;
        try {
            m = Material.getMaterial(Integer.parseInt(s));
        } catch (Exception e) {
        }
        if (m != null) return m;
        return null;
    }
}

interface StringInterpreter {
    /**
     * Interpret a data string into a Byte suitable for MaterialData.
     * <p>
     * The provided string must first be made all uppercase before
     * calling.
     *
     * @param s provided string
     * @return byte of data
     */
    public byte interpret(String s);
}
class TexturedMaterialInterpreter implements StringInterpreter {
    private TexturedMaterial referenceInstance;

    public TexturedMaterialInterpreter(TexturedMaterial instance) {
        referenceInstance = instance;
    }
    @Override
    public byte interpret(String s) {
        referenceInstance.setMaterial(Material.valueOf(s));
        return referenceInstance.getData();
    }
}
class EnumOrdinalMaterialInterpreter implements StringInterpreter {
    private Enum<?>[] values;

    public EnumOrdinalMaterialInterpreter(Enum<?>[] values) {
        this.values = values;
    }
    @Override
    public byte interpret(String s) {
        for (Enum<?> e : values) {
            if (s.equalsIgnoreCase(e.toString())) {
                return (byte) e.ordinal();
            }
        }
        return 0;
    }
}
enum TEMP_WallType {
    COBBLESTONE,
    MOSSY_COBBLESTONE;
}
enum TEMP_AnvilDamage {
    UNDAMAGED,
    SLIGHTLY_DAMAGED,
    VERY_DAMAGED;
}
enum TEMP_QuartzType {
    NORMAL,
    CHISELED,
    PILLAR;
}