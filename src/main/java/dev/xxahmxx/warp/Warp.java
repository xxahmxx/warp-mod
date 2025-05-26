package dev.xxahmxx.warp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Warp implements ModInitializer {
    public static final String MOD_ID = "warp";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(WarpCommandRegistry::registerCommands);
    }

}