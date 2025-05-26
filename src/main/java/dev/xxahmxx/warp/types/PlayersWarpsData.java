package dev.xxahmxx.warp.types;

import dev.xxahmxx.warp.utils.IEntityDataSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayersWarpsData {
    private static final String NBT_KEY = "warp-location-list";
    private static final String WARP_ALREADY_EXIST = "§4Warp §l%s§r§4 already exists.";
    private static final String WARP_SAVED = "§6Warp §b%s§6 saved.";

    private static final String WARP_DONT_EXIST = "§4Warp §l%s§r§4 do not exists.";
    private static final String WARP_DELETED = "§6Warp §l§b%s§r§6 has been deleted.";

    public Map<WarpName, WarpLocation> getWarpListOrCreate(IEntityDataSaver player) {
        return convertToWarpMap(getPlayerWarpData(player));
    }

    public List<String> getWarpsNames(IEntityDataSaver player) {
        return getWarpListOrCreate(player).keySet().stream().map(WarpName::warpName).toList();
    }

    private static NbtCompound getPlayerWarpData(IEntityDataSaver player) {
        NbtCompound playerData = player._$getPlayerData();
        Optional<NbtCompound> playerWarpData = playerData.getCompound(NBT_KEY);
        return playerWarpData.orElseGet(NbtCompound::new);
    }

    public static NbtCompound convertMapToCompound(Map<WarpName, WarpLocation> warpMap) {
        NbtCompound warpMapCompound = new NbtCompound();
        for (Map.Entry<WarpName, WarpLocation> entry : warpMap.entrySet()) {
            WarpName warpName = entry.getKey();
            WarpLocation warpLocation = entry.getValue();
            warpMapCompound.put(warpName.warpName(), warpLocation.toNbt());
        }
        return warpMapCompound;
    }

    public static Map<WarpName, WarpLocation> convertToWarpMap(NbtCompound playerWarpData) {
        HashMap<WarpName, WarpLocation> warpMap = new HashMap<>();
        for (String key : playerWarpData.getKeys()) {
            // Retrieve the NbtCompound for the WarpLocation
            Optional<NbtCompound> warpNbt = playerWarpData.getCompound(key);

            if (warpNbt.isPresent()) {
                // Deserialize WarpLocation from the NbtCompound
                WarpLocation warpLocation = WarpLocation.fromNbt(warpNbt.get());

                // Use the key as WarpName
                WarpName warpName = new WarpName(key);

                // Add to the map
                warpMap.put(warpName, warpLocation);
            }
        }
        return warpMap;
    }

    public void addWarp(IEntityDataSaver player, WarpLocation warp) {
        Map<WarpName, WarpLocation> warps;
        NbtCompound playerData = player._$getPlayerData();
        Optional<NbtCompound> playerWarpData = playerData.getCompound(NBT_KEY);
        warps = playerWarpData.map(PlayersWarpsData::convertToWarpMap).orElseGet(HashMap::new);
        WarpLocation warpLocation = warps.get(new WarpName(warp.warpName()));
        if (warpLocation == null) {
            warps.put(new WarpName(warp.warpName()), warp);
            NbtCompound warpMapCompound = convertMapToCompound(warps);
            playerData.put(NBT_KEY, warpMapCompound);
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.sendMessage(Text.of(WARP_SAVED.formatted(warp.warpName())), true);
        } else {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.sendMessage(Text.of(WARP_ALREADY_EXIST.formatted(warp.warpName())), true);
        }

    }

    public void removeWarp(IEntityDataSaver player, WarpLocation warp) {
        Map<WarpName, WarpLocation> warps = getWarpListOrCreate(player);
        WarpLocation warpLocation = warps.get(new WarpName(warp.warpName()));
        if (warpLocation != null) {
            warps.remove(new WarpName(warp.warpName()));
            NbtCompound warpMapCompound = convertMapToCompound(warps);
            player._$getPlayerData().put(NBT_KEY, warpMapCompound);
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.sendMessage(Text.of(WARP_DELETED.formatted(warp.warpName())), true);
        } else {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.sendMessage(Text.of(WARP_DONT_EXIST.formatted(warp.warpName())), true);
        }
    }

    public Optional<WarpLocation> getWarp(IEntityDataSaver player, String name) {
        return Optional.ofNullable(getWarpListOrCreate(player).get(new WarpName(name)));
    }
}
