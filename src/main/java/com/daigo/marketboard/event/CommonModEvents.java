package com.daigo.marketboard.event;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.market.MarketSavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

@EventBusSubscriber(modid = MarketBoard.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonModEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MarketSavedData data = MarketSavedData.get(player.serverLevel());
            long pendingAmount = data.claimPendingPayment(player.getUUID());

            if (pendingAmount > 0) {
                player.getData(MarketBoard.MONEY).add((int) pendingAmount);
                player.sendSystemMessage(
                        Component.literal("You earned " + pendingAmount + "G from sales while you were offline!")
                                .withStyle(net.minecraft.ChatFormatting.GOLD));
            }
        }
    }
}
