package dev.xxahmxx.warp;

import dev.xxahmxx.warp.types.PlayerName;
import dev.xxahmxx.warp.types.PlayersWarpsData;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class WarpStorage {

    private static final HashMap<PlayerName, PlayersWarpsData> warps = new HashMap<>();

    public static PlayersWarpsData getPlayerWarps(ServerPlayerEntity player) {
        warps.putIfAbsent(new PlayerName(player.getName().getString()), new PlayersWarpsData());
        return warps.get(new PlayerName(player.getName().getString()));
    }
}
