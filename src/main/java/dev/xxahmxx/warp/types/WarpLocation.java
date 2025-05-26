package dev.xxahmxx.warp.types;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record WarpLocation(Vec3d location, RegistryKey<World> worldId, String warpName) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        // Serialize the location (x, y, z coordinates)
        nbt.putDouble("x", location.x);
        nbt.putDouble("y", location.y);
        nbt.putDouble("z", location.z);

        // Serialize the world/dimension ID as a string
        nbt.putString("worldId", worldId.getValue().toString());

        // Serialize the warp name
        nbt.putString("warpName", warpName);

        return nbt;
    }

    public static WarpLocation fromNbt(NbtCompound nbt) {
        // Safely retrieve x, y, z from the NbtCompound
        double x = nbt.getDouble("x").orElseThrow(() -> new IllegalStateException("Missing x value in NBT"));
        double y = nbt.getDouble("y").orElseThrow(() -> new IllegalStateException("Missing y value in NBT"));
        double z = nbt.getDouble("z").orElseThrow(() -> new IllegalStateException("Missing z value in NBT"));
        Vec3d location = new Vec3d(x, y, z);

        // Safely retrieve the worldId as a string
        String worldIdString = nbt.getString("worldId").orElseThrow(() -> new IllegalStateException("Missing worldId in NBT"));
        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(worldIdString));

        // Safely retrieve the warpName
        String warpName = nbt.getString("warpName").orElseThrow(() -> new IllegalStateException("Missing warpName in NBT"));

        // Return the reconstructed WarpLocation
        return new WarpLocation(location, worldKey, warpName);
    }
}
