package com.aeroshide.syncedadvancements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class SyncedAdvancements implements ModInitializer {
    public static final Logger LOG = LogManager.getLogger("AdvancementSync");

    @Override
    public void onInitialize() {
        LOG.info("Loaded SyncAdv");
    }
}
