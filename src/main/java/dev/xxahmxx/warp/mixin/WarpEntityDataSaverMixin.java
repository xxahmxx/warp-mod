package dev.xxahmxx.warp.mixin;

import dev.xxahmxx.warp.Warp;
import dev.xxahmxx.warp.utils.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class WarpEntityDataSaverMixin implements IEntityDataSaver {

    @Unique
    private static final String NBT_TAG = "%s.%s".formatted(Warp.MOD_ID, "warp");
    @Unique
    private NbtCompound playerData;

    @Override
    public NbtCompound _$getPlayerData() {
        if (this.playerData == null) {
            this.playerData = new NbtCompound();
        }
        return playerData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (playerData != null) {
            nbt.put(NBT_TAG, playerData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        nbt.getCompound(NBT_TAG)
                .ifPresentOrElse(
                        (compound) -> playerData = compound,
                        () -> playerData = new NbtCompound()
                );
    }
}
