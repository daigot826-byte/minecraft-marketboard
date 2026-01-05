package com.daigo.marketboard.command;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.gui.MarketBoardMenu;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

public class MarketCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("market")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    player.openMenu(new SimpleMenuProvider(
                            (id, inv, p) -> new MarketBoardMenu(id, inv),
                            Component.literal("Market Board")));
                    return 1;
                }));
    }
}
