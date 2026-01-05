package com.daigo.marketboard.network;

import com.daigo.marketboard.MarketBoard;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AddListingPayload(int price) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddListingPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MarketBoard.MODID, "add_listing"));

    public static final StreamCodec<ByteBuf, AddListingPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AddListingPayload::price,
            AddListingPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
