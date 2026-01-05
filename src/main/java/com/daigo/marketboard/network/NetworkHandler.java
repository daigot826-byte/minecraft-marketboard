package com.daigo.marketboard.network;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.market.MarketListing;
import com.daigo.marketboard.market.MarketSavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

@EventBusSubscriber(modid = MarketBoard.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");

        registrar.playBidirectional(
                AddListingPayload.TYPE,
                AddListingPayload.STREAM_CODEC,
                NetworkHandler::handleAddListing);

        registrar.playBidirectional(
                BuyItemPayload.TYPE,
                BuyItemPayload.STREAM_CODEC,
                NetworkHandler::handleBuyItem);

        registrar.playBidirectional(
                SyncListingsPayload.TYPE,
                SyncListingsPayload.STREAM_CODEC,
                NetworkHandler::handleSyncListings);
    }

    private static void handleBuyItem(final BuyItemPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                MarketSavedData data = MarketSavedData.get(player.serverLevel());

                data.getListingById(payload.listingId()).ifPresent(listing -> {
                    int price = listing.price();
                    var money = player.getData(MarketBoard.MONEY);

                    if (money.getAmount() >= price) {
                        // Transaction
                        money.remove(price);

                        // Give item (or drop if inventory full)
                        ItemStack item = listing.item().copy();
                        if (!player.getInventory().add(item)) {
                            player.drop(item, false);
                        }

                        // Seller Logic (Online check or Pending)
                        ServerPlayer seller = player.serverLevel().getServer().getPlayerList()
                                .getPlayer(listing.sellerId());

                        // Transaction Logic
                        if (seller != null) {
                            // Seller Online: Direct Transfer
                            seller.getData(MarketBoard.MONEY).add(price);
                            seller.sendSystemMessage(
                                    net.minecraft.network.chat.Component.literal("Item sold! +" + price + "G"));
                        } else {
                            // Seller Offline: Store as Pending Payment
                            data.addPendingPayment(listing.sellerId(), price);
                            // Optional: Log or debugging
                            // System.out.println("Seller offline. Stored " + price + "G for " +
                            // listing.sellerName());
                        }

                        // Remove and Sync
                        data.removeListing(listing.id());

                        // Sync to ALL players
                        net.neoforged.neoforge.network.PacketDistributor
                                .sendToAllPlayers(new SyncListingsPayload(data.getListings()));

                        player.sendSystemMessage(
                                net.minecraft.network.chat.Component.literal("Bought item for " + price + "G"));
                    } else {
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Not enough money!")
                                .withStyle(net.minecraft.ChatFormatting.RED));
                    }
                });
            }
        });
    }

    private static void handleAddListing(final AddListingPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            System.out.println("DEBUG: handleAddListing received. Price: " + payload.price()); // DEBUG
            if (context.player() instanceof ServerPlayer player) {
                // Simplified Logic: Take item from main hand OR carried (cursor)
                ItemStack item = player.getMainHandItem();
                if (item.isEmpty()) {
                    item = player.containerMenu.getCarried();
                }

                System.out.println("DEBUG: Item to list: " + item); // DEBUG
                if (!item.isEmpty()) {
                    ItemStack listedItem = item.copy();
                    UUID listingId = UUID.randomUUID();
                    MarketListing listing = new MarketListing(
                            listingId,
                            listedItem,
                            payload.price(),
                            player.getUUID(),
                            player.getName().getString(),
                            System.currentTimeMillis());

                    MarketSavedData data = MarketSavedData.get(player.serverLevel());
                    data.addListing(listing);
                    System.out.println("DEBUG: Listing added to data. Total listings: " + data.getListings().size()); // DEBUG

                    item.setCount(0); // Remove item

                    // Sync to ALL players
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToAllPlayers(new SyncListingsPayload(data.getListings()));

                    player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal("Listed item for " + payload.price() + "G"));
                }
            }
        });
    }

    private static void handleSyncListings(final SyncListingsPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Client side handling
            com.daigo.marketboard.gui.ClientMarketData.getInstance().updateListings(payload.listings());
        });
    }
}
