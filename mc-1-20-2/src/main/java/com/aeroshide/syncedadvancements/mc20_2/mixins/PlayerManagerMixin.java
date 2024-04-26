package com.aeroshide.syncedadvancements.mc20_2.mixins;

import net.minecraft.advancement.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        PlayerManager players = (PlayerManager) (Object) this;
        MinecraftServer server = player.getServer();
        if (server != null) {
            ServerAdvancementLoader advancementManager = server.getAdvancementLoader();
            for (ServerPlayerEntity otherPlayer : players.getPlayerList()) {
                if (otherPlayer != player) {
                    PlayerAdvancementTracker tracker = otherPlayer.getAdvancementTracker();
                    for (AdvancementEntry advancement : advancementManager.getAdvancements()) {
                        AdvancementProgress advancementProgress = tracker.getProgress(advancement);
                        if (advancementProgress.isDone()) {
                            for (String s : advancementProgress.getObtainedCriteria()) {
                                player.getAdvancementTracker().grantCriterion(advancement, s);
                            }
                        }
                    }
                }
            }
            for (ServerPlayerEntity otherPlayer : players.getPlayerList()) {
                if (otherPlayer != player) {
                    PlayerAdvancementTracker tracker = player.getAdvancementTracker();
                    for (AdvancementEntry advancement : advancementManager.getAdvancements()) {
                        AdvancementProgress advancementProgress = tracker.getProgress(advancement);
                        if (advancementProgress.isDone()) {
                            for (String s : advancementProgress.getObtainedCriteria()) {
                                otherPlayer.getAdvancementTracker().grantCriterion(advancement, s);
                            }
                        }
                    }
                }
            }
        }
    }
}




