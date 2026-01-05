package com.daigo.marketboard.network;

import com.daigo.marketboard.MarketBoard;
import com.daigo.marketboard.market.MarketListing;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SyncListingsPayload(List<MarketListing> listings) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncListingsPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MarketBoard.MODID, "sync_listings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncListingsPayload> STREAM_CODEC = StreamCodec.composite(
            MarketListing.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncListingsPayload::listings,
            SyncListingsPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
