package com.daigo.marketboard.market;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.item.ItemStack;

import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.UUID;

public record MarketListing(UUID id, ItemStack item, int price, UUID sellerId, String sellerName, long timestamp) {
    public static final Codec<MarketListing> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(MarketListing::id),
            ItemStack.CODEC.fieldOf("item").forGetter(MarketListing::item),
            Codec.INT.fieldOf("price").forGetter(MarketListing::price),
            UUIDUtil.CODEC.fieldOf("sellerId").forGetter(MarketListing::sellerId),
            Codec.STRING.fieldOf("sellerName").forGetter(MarketListing::sellerName),
            Codec.LONG.fieldOf("timestamp").forGetter(MarketListing::timestamp)).apply(instance, MarketListing::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MarketListing> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, MarketListing::id,
            ItemStack.STREAM_CODEC, MarketListing::item,
            ByteBufCodecs.INT, MarketListing::price,
            UUIDUtil.STREAM_CODEC, MarketListing::sellerId,
            ByteBufCodecs.STRING_UTF8, MarketListing::sellerName,
            ByteBufCodecs.VAR_LONG, MarketListing::timestamp,
            MarketListing::new);
}
