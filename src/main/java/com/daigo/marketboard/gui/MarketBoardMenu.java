package com.daigo.marketboard.gui;

import com.daigo.marketboard.MarketBoard;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MarketBoardMenu extends AbstractContainerMenu {

    public MarketBoardMenu(int containerId, Inventory playerInventory) {
        super(MarketBoard.MARKET_MENU.get(), containerId);

        // Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        if (playerInventory.player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            com.daigo.marketboard.market.MarketSavedData data = com.daigo.marketboard.market.MarketSavedData
                    .get(serverPlayer.serverLevel());
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer,
                    new com.daigo.marketboard.network.SyncListingsPayload(data.getListings()));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // Implementation needed later
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
