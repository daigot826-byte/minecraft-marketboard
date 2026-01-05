package com.daigo.marketboard.command;

import com.daigo.marketboard.MarketBoard;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class BalanceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("balance")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    int amount = player.getData(MarketBoard.MONEY).getAmount();
                    player.sendSystemMessage(Component.literal("Currency: " + amount));
                    return 1;
                })
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", com.mojang.brigadier.arguments.IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    int amount = com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(context,
                                            "amount");
                                    player.getData(MarketBoard.MONEY).add(amount);
                                    player.sendSystemMessage(
                                            Component.literal("Added " + amount + " currency. New Balance: "
                                                    + player.getData(MarketBoard.MONEY).getAmount()));
                                    return 1;
                                })))
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", com.mojang.brigadier.arguments.IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    int amount = com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(context,
                                            "amount");
                                    player.getData(MarketBoard.MONEY).setAmount(amount);
                                    player.sendSystemMessage(Component.literal("Set currency to " + amount + "."));
                                    return 1;
                                }))));
    }
}
