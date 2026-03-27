package com.aeroshide.syncedadvancements.mixins;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("RETURN"), require = 0)
    private void onPlayerConnect(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        PlayerList players = (PlayerList) (Object) this;
        MinecraftServer server = players.getServer();
        if (server != null) {
            ServerAdvancementManager advancementManager = server.getAdvancements();
            for (ServerPlayer otherPlayer : players.getPlayers()) {
                if (otherPlayer != player) {
                    PlayerAdvancements tracker = otherPlayer.getAdvancements();
                    for (AdvancementHolder advancement : advancementManager.getAllAdvancements()) {
                        AdvancementProgress advancementProgress = tracker.getOrStartProgress(advancement);
                        if (advancementProgress.isDone()) {
                            for (String s : advancementProgress.getCompletedCriteria()) {
                                player.getAdvancements().award(advancement, s);
                            }
                        }
                    }
                }
            }
            for (ServerPlayer otherPlayer : players.getPlayers()) {
                if (otherPlayer != player) {
                    PlayerAdvancements tracker = player.getAdvancements();
                    for (AdvancementHolder advancement : advancementManager.getAllAdvancements()) {
                        AdvancementProgress advancementProgress = tracker.getOrStartProgress(advancement);
                        if (advancementProgress.isDone()) {
                            for (String s : advancementProgress.getCompletedCriteria()) {
                                otherPlayer.getAdvancements().award(advancement, s);
                            }
                        }
                    }
                }
            }
        }
    }
}




