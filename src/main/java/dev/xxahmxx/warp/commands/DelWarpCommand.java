package dev.xxahmxx.warp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xxahmxx.warp.WarpStorage;
import dev.xxahmxx.warp.types.PlayersWarpsData;
import dev.xxahmxx.warp.types.WarpLocation;
import dev.xxahmxx.warp.utils.IEntityDataSaver;
import dev.xxahmxx.warp.utils.MessageSenderUtil;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class DelWarpCommand implements Command<ServerCommandSource> {
    private static final String WARP_DONT_EXIST = "§4Warp §l%s§r§4 do not exists.";
    private static final String WARP_DELETED = "§6Warp §l§b%s§r§6 has been deleted.";

    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        String warpName = StringArgumentType.getString(commandContext, "warp-name");
        ServerCommandSource source = commandContext.getSource();
        ServerPlayerEntity player = source.getPlayerOrThrow();
        PlayersWarpsData playerWarps = WarpStorage.getPlayerWarps(player);
        playerWarps.getWarp((IEntityDataSaver) player, warpName)
                .ifPresentOrElse(
                        (warp) -> deleteWarp(commandContext, warp, playerWarps, warpName),
                        () -> MessageSenderUtil.sendMessage(commandContext, WARP_DONT_EXIST, warpName)
                );
        return 0;
    }

    private static void deleteWarp(CommandContext<ServerCommandSource> commandContext, WarpLocation warp, PlayersWarpsData playerWarps, String warpName) {
        playerWarps.removeWarp((IEntityDataSaver) commandContext.getSource().getPlayer(), warp);
        MessageSenderUtil.sendMessage(commandContext, WARP_DELETED, warpName);
    }
}
