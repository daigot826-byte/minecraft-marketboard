package com.daigo.marketboard.event;

import com.daigo.marketboard.MarketBoard;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber(modid = MarketBoard.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CurrencyEventHandler {
    @SubscribeEvent
    public static void onPickupXp(PlayerXpEvent.PickupXp event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            int xp = event.getOrb().getValue();
            player.getData(MarketBoard.MONEY).add(xp);
        }
    }
}
