package com.daigo.marketboard.network;

import com.daigo.marketboard.MarketBoard;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record BuyItemPayload(UUID listingId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BuyItemPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MarketBoard.MODID, "buy_item"));

    public static final StreamCodec<ByteBuf, BuyItemPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BuyItemPayload::listingId,
            BuyItemPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
