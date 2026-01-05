package com.daigo.marketboard.market;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketSavedData extends SavedData {
    private static final String DATA_NAME = "marketboard_data";
    private final List<MarketListing> listings = new ArrayList<>();
    private final java.util.Map<UUID, Long> pendingPayments = new java.util.HashMap<>();

    public static MarketSavedData get(ServerLevel level) {
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(
                MarketSavedData::new,
                MarketSavedData::load,
                null), DATA_NAME);
    }

    public MarketSavedData() {
    }

    public static MarketSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        MarketSavedData data = new MarketSavedData();
        data.listings.addAll(MarketListing.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("listings")).result()
                .orElse(new ArrayList<>()));

        CompoundTag pendingTag = tag.getCompound("pendingPayments");
        for (String key : pendingTag.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(key);
                long amount = pendingTag.getLong(key);
                data.pendingPayments.put(uuid, amount);
            } catch (IllegalArgumentException e) {
                // Ignore invalid UUIDs
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        MarketListing.CODEC.listOf().encodeStart(NbtOps.INSTANCE, listings).resultOrPartial()
                .ifPresent(t -> tag.put("listings", t));

        CompoundTag pendingTag = new CompoundTag();
        pendingPayments.forEach((uuid, amount) -> pendingTag.putLong(uuid.toString(), amount));
        tag.put("pendingPayments", pendingTag);

        return tag;
    }

    public void addListing(MarketListing listing) {
        listings.add(listing);
        setDirty();
    }

    public void removeListing(UUID listingId) {
        listings.removeIf(l -> l.id().equals(listingId));
        setDirty();
    }

    public java.util.Optional<MarketListing> getListingById(UUID listingId) {
        return listings.stream().filter(l -> l.id().equals(listingId)).findFirst();
    }

    public List<MarketListing> getListings() {
        return new ArrayList<>(listings);
    }

    public void addPendingPayment(UUID sellerId, long amount) {
        pendingPayments.merge(sellerId, amount, Long::sum);
        setDirty();
    }

    public long claimPendingPayment(UUID sellerId) {
        Long amount = pendingPayments.remove(sellerId);
        if (amount != null) {
            setDirty();
            return amount;
        }
        return 0L;
    }
}
