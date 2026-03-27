package com.aeroshide.syncedadvancements.mixins;


import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow
    private ServerPlayer player;
    @Unique
    private boolean isGranting = false;

    @Inject(method = "award", at = @At("HEAD"), require = 0)
    private void onAdvancement(AdvancementHolder advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (!isGranting && advancement != null) {
            isGranting = true;
            PlayerList players = player.server.getPlayerList();
            for (ServerPlayer player : players.getPlayers()) {
                PlayerAdvancements tracker = player.getAdvancements();
                tracker.award(advancement, criterionName);
            }
            isGranting = false;
        }
    }
}


