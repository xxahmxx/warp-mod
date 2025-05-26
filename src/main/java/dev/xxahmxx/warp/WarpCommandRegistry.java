package dev.xxahmxx.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.xxahmxx.warp.commands.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;


public class WarpCommandRegistry {

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        RootCommandNode<ServerCommandSource> rootNode = dispatcher.getRoot();
        LiteralCommandNode<ServerCommandSource> warpHelpNode = helpCommand();
        LiteralCommandNode<ServerCommandSource> warpCommandRootNode = topWarpCommand();
        warpCommandRootNode.addChild(warpHelpNode);
        warpCommandRootNode.addChild(teleportCommand());
        warpCommandRootNode.addChild(setWarpCommand());
        warpCommandRootNode.addChild(deleteWarpCommand());
        warpCommandRootNode.addChild(listWarpsCommand());
        rootNode.addChild(warpCommandRootNode);
    }

    private static CommandNode<ServerCommandSource> deleteWarpCommand() {
        return CommandManager.literal("del")
                .then(CommandManager.argument("warp-name", StringArgumentType.string())
                        .executes(new DelWarpCommand())
                        .suggests(new PlayerWarpSuggestionProvider()))
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> topWarpCommand() {
        return CommandManager.literal("warp")
                .executes(helpCommand().getCommand())
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> helpCommand() {
        return CommandManager.literal("help")
                .executes(new ModHelpCommand())
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> setWarpCommand() {
        return CommandManager.literal("set")
                .then(CommandManager.argument("warp-name", StringArgumentType.string())
                        .executes(new SetWarpCommand()))
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> teleportCommand() {
        return CommandManager.literal("tp")
                .then(CommandManager.argument("warp-name", StringArgumentType.string())
                        .suggests(new PlayerWarpSuggestionProvider())
                        .executes(new TeleportCommand()))
                .build();
    }

    private static LiteralCommandNode<ServerCommandSource> listWarpsCommand() {
        return CommandManager.literal("list")
                .executes(new ListWarpsCommand())
                .build();
    }
}
