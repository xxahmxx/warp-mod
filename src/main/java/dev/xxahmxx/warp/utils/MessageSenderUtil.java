package dev.xxahmxx.warp.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MessageSenderUtil {
    public static void sendMessage(CommandContext<ServerCommandSource> commandContext, String errorMessage) {
        sendMessage(commandContext, errorMessage, new String[0]);
    }

    public static void sendMessage(CommandContext<ServerCommandSource> commandContext, String message, String... messageValue) {
        commandContext.getSource()
                .sendFeedback(() -> Text.of(message.formatted((Object[]) messageValue)), false);
    }
}
