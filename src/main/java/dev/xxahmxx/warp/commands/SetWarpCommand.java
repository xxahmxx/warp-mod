package dev.xxahmxx.warp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xxahmxx.warp.WarpStorage;
import dev.xxahmxx.warp.types.PlayersWarpsData;
import dev.xxahmxx.warp.types.WarpLocation;
import dev.xxahmxx.warp.utils.IEntityDataSaver;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetWarpCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        String warpName = StringArgumentType.getString(commandContext, "warp-name");
        ServerPlayerEntity player = commandContext.getSource().getPlayerOrThrow();
        PlayersWarpsData playerWarps = WarpStorage.getPlayerWarps(player);
        playerWarps.addWarp((IEntityDataSaver) player, new WarpLocation(player.getPos(),
                player.getServerWorld().getRegistryKey(), warpName));
        return 0;
    }
}
