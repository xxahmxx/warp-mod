package dev.xxahmxx.warp.utils;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class WorldUtils {
    public static String getWorldName(RegistryKey<World> world) {
        String worldName = world.getValue().toString().split(":")[1];
        return switch (worldName) {
            case "the_nether" -> "nether";
            case "the_end" -> "end";
            case "overworld" -> "overworld";
            default -> worldName;
        };
    }
}
