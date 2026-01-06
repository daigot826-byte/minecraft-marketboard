package com.daigo.marketboard.gui;

import com.daigo.marketboard.market.MarketListing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MarketListingList extends ObjectSelectionList<MarketListingList.Entry> {
    private final MarketBoardScreen parentScreen;

    public MarketListingList(MarketBoardScreen parentScreen, Minecraft minecraft, int width, int height, int top,
            int itemHeight) {
        super(minecraft, width, height, top, itemHeight);
        this.parentScreen = parentScreen;
    }

    public void updateList(List<MarketListing> listings) {
        this.clearEntries();
        for (MarketListing listing : listings) {
            this.addEntry(new Entry(listing));
        }
    }

    @Override
    public int getRowWidth() {
        return 160;
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final MarketListing listing;

        public Entry(MarketListing listing) {
            this.listing = listing;
        }

        @Override
        public Component getNarration() {
            return Component.literal(listing.item().getHoverName().getString());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX,
                int mouseY, boolean isMouseOver, float partialTick) {
            // Background Highlighting
            if (isMouseOver) {
                guiGraphics.fill(left, top, left + width, top + height, 0x80FFFFFF);
            }

            // Item Icon
            guiGraphics.renderItem(listing.item(), left + 2, top + 2);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, listing.item(), left + 2, top + 2);

            // Text Info
            String itemName = listing.item().getHoverName().getString();
            String price = listing.price() + " G";
            String seller = listing.sellerName();

            guiGraphics.drawString(Minecraft.getInstance().font, itemName, left + 24, top + 2, 0xFFFFFF, false);
            guiGraphics.drawString(Minecraft.getInstance().font, price, left + 24, top + 12, 0xFFFF55, false);
            guiGraphics.drawString(Minecraft.getInstance().font, seller, left + 100, top + 6, 0xAAAAAA, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            parentScreen.setSelectedListing(this.listing);
            return true;
        }
    }
}
