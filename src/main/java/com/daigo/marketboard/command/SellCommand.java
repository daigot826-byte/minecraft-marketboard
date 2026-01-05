package com.daigo.marketboard.command;

import com.daigo.marketboard.network.AddListingPayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SellCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sell")
                .then(Commands.argument("price", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            int price = IntegerArgumentType.getInteger(context, "price");

                            // Direct logic call (bypassing packet for simplicity in command, OR emulate
                            // packet)
                            // Since NetworkHandler is logic, let's just trigger logic via packet to
                            // simulate real flow?
                            // But command is server-side. Packet is Client->Server.
                            // If we run this command, we are ALREADY on server.
                            // So we can't send "C->S" packet from server.
                            // We should call the logic directly or trigger the packet handler logic.
                            // However, avoiding code duplication is good.
                            // Let's just create a quick helper or duplicate logic for this test command.

                            // Actually, let's keep it simple and just do what the handler does.
                            // But waiting, NetworkHandler.handleAddListing is private.
                            // Let's make a public helper in NetworkHandler or just duplicate for test.
                            // Duplication for test command is fine.

                            if (!player.getMainHandItem().isEmpty()) {
                                // Send fake packet to self? No.
                                // Just logic.

                                net.minecraft.world.item.ItemStack item = player.getMainHandItem();
                                net.minecraft.world.item.ItemStack listedItem = item.copy();
                                java.util.UUID listingId = java.util.UUID.randomUUID();
                                com.daigo.marketboard.market.MarketListing listing = new com.daigo.marketboard.market.MarketListing(
                                        listingId,
                                        listedItem,
                                        price,
                                        player.getUUID(),
                                        player.getName().getString(),
                                        System.currentTimeMillis());

                                com.daigo.marketboard.market.MarketSavedData data = com.daigo.marketboard.market.MarketSavedData
                                        .get(player.serverLevel());
                                data.addListing(listing);

                                item.setCount(0);

                                PacketDistributor.sendToAllPlayers(
                                        new com.daigo.marketboard.network.SyncListingsPayload(data.getListings()));

                                player.sendSystemMessage(
                                        net.minecraft.network.chat.Component.literal("Listed item for " + price + "G"));
                            } else {
                                player.sendSystemMessage(
                                        net.minecraft.network.chat.Component.literal("Hold an item to sell."));
                            }

                            return 1;
                        })));
    }
}
