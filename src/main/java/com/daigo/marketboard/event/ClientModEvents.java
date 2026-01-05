package com.daigo.marketboard.event;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.gui.MarketBoardScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = MarketBoard.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(MarketBoard.MARKET_MENU.get(), MarketBoardScreen::new);
    }
}
