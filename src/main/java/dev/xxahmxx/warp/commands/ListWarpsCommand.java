package dev.xxahmxx.warp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xxahmxx.warp.WarpStorage;
import dev.xxahmxx.warp.types.PlayersWarpsData;
import dev.xxahmxx.warp.types.WarpLocation;
import dev.xxahmxx.warp.types.WarpName;
import dev.xxahmxx.warp.utils.IEntityDataSaver;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Map;
import java.util.stream.Collectors;

public class ListWarpsCommand implements Command<ServerCommandSource> {

    private static final String NO_WARPS = "§4You don't have saved warps!";

    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerPlayerEntity player = commandContext.getSource().getPlayerOrThrow();
        PlayersWarpsData warps = WarpStorage.getPlayerWarps(player);
        Map<WarpName, WarpLocation> warpList = warps.getWarpListOrCreate((IEntityDataSaver) player);
        if (warpList.isEmpty()) {
            commandContext.getSource().sendFeedback(() -> Text.of(NO_WARPS), false);
        } else {
            String warpsList = warpList.values().stream()
                    .map(warp -> "%s in %s".formatted(warp.warpName(), getWorldName(warp.worldId())))
                    .collect(Collectors.joining("\n"));
            player.sendMessage(Text.of("""
                    §6Here is a list of warps:
                    §9%s
                    """.formatted(warpsList)), false);
        }
        return 0;
    }

    private String getWorldName(RegistryKey<World> world) {
        return world.getValue().toString().split(":")[1];
    }
}
