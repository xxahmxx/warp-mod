package dev.xxahmxx.warp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ModHelpCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendFeedback(() -> Text.literal("""
                §6Welcome to Warp Mod!
                It's teleportation mod for client only.
                
                Here are available commands:
                §3/warp help §b- shows this message
                §3/warp jump §b- teleport player few block above
                §3/warp §9<warp-name> §b- teleport player to warp with given name
                §3/warp set §9<warp-name> §b-  save warp under given name.
                §3/warp del §9<warp-name> §b- delete warp with given name
                """), false);
        return 0;
    }
}
