package com.daigo.marketboard.event;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.command.BalanceCommand;
import com.daigo.marketboard.command.MarketCommand;
import com.daigo.marketboard.command.SellCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = MarketBoard.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommandEventHandler {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        BalanceCommand.register(event.getDispatcher());
        MarketCommand.register(event.getDispatcher());
        SellCommand.register(event.getDispatcher());
    }
}
