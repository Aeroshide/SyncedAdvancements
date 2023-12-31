package com.aeroshide.syncedadvancements.mc20_2.mixins;


import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;
    @Unique
    private boolean isGranting = false;

    @Inject(method = "grantCriterion", at = @At("HEAD"), require = 0)
    private void onAdvancement(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (!isGranting && advancement != null) {
            isGranting = true;
            PlayerManager players = owner.server.getPlayerManager();
            for (ServerPlayerEntity player : players.getPlayerList()) {
                if (player != owner) {
                    PlayerAdvancementTracker tracker = player.getAdvancementTracker();
                    tracker.grantCriterion(advancement, criterionName);
                }
            }
            isGranting = false;
        }
    }
}


