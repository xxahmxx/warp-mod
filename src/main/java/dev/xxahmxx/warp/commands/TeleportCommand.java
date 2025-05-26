package dev.xxahmxx.warp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xxahmxx.warp.types.PlayersWarpsData;
import dev.xxahmxx.warp.utils.IEntityDataSaver;
import dev.xxahmxx.warp.utils.MessageSenderUtil;
import dev.xxahmxx.warp.WarpStorage;
import dev.xxahmxx.warp.types.WarpLocation;
import dev.xxahmxx.warp.utils.WorldUtils;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

public class TeleportCommand implements Command<ServerCommandSource> {

    private static final SoundEvent SOUND_EVENT = SoundEvent.of(Identifier.of("minecraft:entity.enderman.teleport"));
    private static final String NO_WARP_MESSAGE = "§4Warp §l%s§r§4 does not exist.";
    private static final String NO_PLAYER_MESSAGE = "§4Player does not exist.";
    private static final String TELEPORTED_MESSAGE = "§6Teleported to §b%s§6 in §b%s§6";

    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerCommandSource source = commandContext.getSource();
        String warpName = commandContext.getArgument("warp-name", String.class);
        ServerPlayerEntity player = source.getPlayerOrThrow();
        PlayersWarpsData playersWarpsData = WarpStorage.getPlayerWarps(player);

        playersWarpsData.getWarp((IEntityDataSaver) player, warpName).ifPresentOrElse(
                warp -> teleport(commandContext, warp),
                () -> MessageSenderUtil.sendMessage(commandContext, NO_WARP_MESSAGE, warpName)
        );
        return 0;
    }

    private void teleport(CommandContext<ServerCommandSource> commandContext, WarpLocation warp) {
        try {
            Vec3d location = warp.location();
            ServerPlayerEntity player = commandContext.getSource().getPlayerOrThrow();
            ServerWorld world = getWorld(commandContext, warp.worldId());
            player.teleport(world, location.x, location.y, location.z, Set.of(PositionFlag.DELTA_X), 1, 1, false);
            player.playSound(SOUND_EVENT);
            player.sendMessage(Text.of(TELEPORTED_MESSAGE.formatted(
                    warp.warpName(),
                    WorldUtils.getWorldName(warp.worldId()))), true);
        } catch (CommandSyntaxException ex) {
            MessageSenderUtil.sendMessage(commandContext, NO_PLAYER_MESSAGE);
        }
    }

    private static ServerWorld getWorld(CommandContext<ServerCommandSource> commandContext, RegistryKey<World> key) {
        return commandContext.getSource().getServer().getWorld(key);
    }
}
