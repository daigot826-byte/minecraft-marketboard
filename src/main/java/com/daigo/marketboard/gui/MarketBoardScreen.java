package com.daigo.marketboard.gui;

import com.daigo.marketboard.MarketBoard;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MarketBoardScreen extends AbstractContainerScreen<MarketBoardMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MarketBoard.MODID,
            "textures/gui/market.png");

    public MarketBoardScreen(MarketBoardMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    private net.minecraft.client.gui.components.Button sellButton;
    private net.minecraft.client.gui.components.EditBox priceInput;
    private net.minecraft.client.gui.components.Button confirmSellButton;
    private net.minecraft.client.gui.components.Button cancelSellButton;
    private boolean isSellMode = false;

    private net.minecraft.client.gui.components.Button closeButton;
    private net.minecraft.client.gui.components.Button confirmBuyButton;
    private net.minecraft.client.gui.components.Button cancelBuyButton;
    private boolean isBuyMode = false;
    private com.daigo.marketboard.market.MarketListing selectedListing;

    @Override
    protected void init() {
        super.init();
        int startX = this.leftPos;
        int startY = this.topPos;

        // "Sell Item" Mode Toggle Button - MOVED TO HEADER to avoid overlap
        this.sellButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Sell"), button -> {
                    this.isSellMode = true;
                    this.isBuyMode = false;
                    updateWidgetVisibility();
                }).bounds(startX + 110, startY - 22, 40, 20).build());

        // Close Button (X)
        this.closeButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("X"), button -> {
                    this.onClose();
                }).bounds(startX + 152, startY - 22, 20, 20).build());

        // Price Input
        this.priceInput = this.addRenderableWidget(new net.minecraft.client.gui.components.EditBox(this.font,
                startX + 38, startY + 85, 100, 20, Component.literal("Price")));
        this.priceInput.setMaxLength(7);
        this.priceInput.setFilter(s -> s.matches("\\d*")); // Only numbers

        // Confirm Sell Button
        this.confirmSellButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Confirm"), button -> {
                    String priceStr = this.priceInput.getValue();
                    net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft
                            .getInstance().player;

                    net.minecraft.world.item.ItemStack currentHand = player.getMainHandItem();
                    net.minecraft.world.item.ItemStack carried = player.containerMenu.getCarried();

                    // Verify item matches current hand OR carried
                    boolean handEmpty = currentHand.isEmpty();
                    boolean carriedEmpty = carried.isEmpty();
                    boolean hasItem = !handEmpty || !carriedEmpty;

                    if (!priceStr.isEmpty() && hasItem) {
                        try {
                            int price = Integer.parseInt(priceStr);
                            net.neoforged.neoforge.network.PacketDistributor
                                    .sendToServer(new com.daigo.marketboard.network.AddListingPayload(price));
                            this.isSellMode = false;
                            this.priceInput.setValue("");
                            updateWidgetVisibility();
                            player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                        } catch (NumberFormatException e) {
                            // Invalid price format
                        }
                    } else {
                        // Validation Failed
                        player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 0.5F, 0.5F);
                    }
                }).bounds(startX + 38, startY + 110, 100, 20).build());

        // Cancel Sell Button
        this.cancelSellButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Cancel"), button -> {
                    this.isSellMode = false;
                    updateWidgetVisibility();
                }).bounds(startX + 38, startY + 135, 100, 20).build());

        // Confirm Buy Button
        this.confirmBuyButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Confirm"), button -> {
                    if (this.selectedListing != null) {
                        net.neoforged.neoforge.network.PacketDistributor
                                .sendToServer(
                                        new com.daigo.marketboard.network.BuyItemPayload(this.selectedListing.id()));
                        net.minecraft.client.Minecraft.getInstance().player
                                .playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                    }
                    this.isBuyMode = false;
                    this.selectedListing = null;
                    updateWidgetVisibility();
                }).bounds(startX + 38, startY + 110, 100, 20).build());

        // Cancel Buy Button
        this.cancelBuyButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Cancel"), button -> {
                    this.isBuyMode = false;
                    this.selectedListing = null;
                    updateWidgetVisibility();
                }).bounds(startX + 38, startY + 135, 100, 20).build());

        updateWidgetVisibility();
    }

    private void updateWidgetVisibility() {
        // Sell Mode Widgets
        this.sellButton.visible = !isSellMode && !isBuyMode;
        this.priceInput.visible = isSellMode;
        this.confirmSellButton.visible = isSellMode;
        this.cancelSellButton.visible = isSellMode;

        // Buy Mode Widgets
        this.confirmBuyButton.visible = isBuyMode;
        this.cancelBuyButton.visible = isBuyMode;

        // Always visible
        this.closeButton.visible = true;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight,
                0xFFC6C6C6);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (isSellMode) {
            // Render Sell Mode Overlay (High Z-index to cover slots and other widgets)
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 600);

            // Dark background covering the whole container
            guiGraphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight,
                    0xFF303030);

            // Labels - Removed "1. Hold Item" text as requested
            guiGraphics.drawCenteredString(this.font, "Sell Mode", this.leftPos + 88, this.topPos + 25, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "Enter Price:", this.leftPos + 88, this.topPos + 75, 0xFFFFFF);

            // Item (LIVE or CARRIED)
            net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
            net.minecraft.world.item.ItemStack heldItem = player.getMainHandItem();
            if (heldItem.isEmpty()) {
                heldItem = player.containerMenu.getCarried();
            }

            if (!heldItem.isEmpty()) {
                // Moved item display up to fill the gap left by removed text
                guiGraphics.renderItem(heldItem, this.leftPos + 80, this.topPos + 45);
                guiGraphics.renderItemDecorations(this.font, heldItem, this.leftPos + 80, this.topPos + 45);
                guiGraphics.drawCenteredString(this.font, heldItem.getHoverName(), this.leftPos + 88,
                        this.topPos + 63,
                        0xAAAAAA);
            }

            // Render widgets manually for Z-index
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 50);
            this.priceInput.render(guiGraphics, mouseX, mouseY, partialTick);
            this.confirmSellButton.render(guiGraphics, mouseX, mouseY, partialTick);
            this.cancelSellButton.render(guiGraphics, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();

            guiGraphics.pose().popPose(); // Overlay

        } else if (isBuyMode && selectedListing != null) {
            // Render Buy Confirmation Overlay
            renderOverlay(guiGraphics, "Confirm Purchase");

            // Buy Mode Specifics
            guiGraphics.drawCenteredString(this.font, "Buy for " + selectedListing.price() + "G?", this.leftPos + 88,
                    this.topPos + 60,
                    0xFFFFFF);

            // Render Item
            guiGraphics.renderItem(selectedListing.item(), this.leftPos + 80, this.topPos + 40);
            guiGraphics.renderItemDecorations(this.font, selectedListing.item(), this.leftPos + 80, this.topPos + 40);
            guiGraphics.drawCenteredString(this.font, selectedListing.item().getHoverName(), this.leftPos + 88,
                    this.topPos + 80,
                    0xAAAAAA);

            // Render widgets manually
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 50);
            this.confirmBuyButton.render(guiGraphics, mouseX, mouseY, partialTick);
            this.cancelBuyButton.render(guiGraphics, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();

            guiGraphics.pose().popPose(); // Overlay

        } else {
            // Render Market List
            int startX = this.leftPos + 8;
            int startY = this.topPos + 18;
            int rowHeight = 20;

            java.util.List<com.daigo.marketboard.market.MarketListing> listings = ClientMarketData.getInstance()
                    .getListings();

            for (int i = 0; i < Math.min(listings.size(), 3); i++) {
                com.daigo.marketboard.market.MarketListing listing = listings.get(i);
                int y = startY + (i * rowHeight);

                // Render Item
                guiGraphics.renderItem(listing.item(), startX, y);
                guiGraphics.renderItemDecorations(this.font, listing.item(), startX, y);

                // Render Info
                String text = listing.item().getHoverName().getString() + " - " + listing.price() + "G - "
                        + listing.sellerName();
                guiGraphics.drawString(this.font, text, startX + 20, y + 4, 4210752, false);

                // Hover Highlight
                if (mouseX >= startX && mouseX <= startX + 160 && mouseY >= y && mouseY < y + rowHeight) {
                    guiGraphics.fill(startX, y, startX + 160, y + rowHeight, 0x80FFFFFF);
                }
            }
        }
    }

    private void renderOverlay(GuiGraphics guiGraphics, String title) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 600); // 600 Z to cover slots

        // Dark background
        guiGraphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight,
                0xFF303030);

        // Title
        guiGraphics.drawCenteredString(this.font, title, this.leftPos + 88, this.topPos + 20, 0xFFFFFF);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!isSellMode && !isBuyMode) {
            guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
            guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY,
                    4210752, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Always handle Close Button first
        if (this.closeButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (isSellMode) {
            // Sell Mode Interactions
            boolean clickedCallback = false;
            if (this.priceInput.mouseClicked(mouseX, mouseY, button)) {
                this.setFocused(this.priceInput);
                clickedCallback = true;
            } else if (this.confirmSellButton.mouseClicked(mouseX, mouseY, button)) {
                clickedCallback = true;
            } else if (this.cancelSellButton.mouseClicked(mouseX, mouseY, button)) {
                clickedCallback = true;
            }
            if (!clickedCallback)
                this.setFocused(null);
            return true;
        }

        if (isBuyMode) {
            // Buy Mode Interactions
            if (this.confirmBuyButton.mouseClicked(mouseX, mouseY, button))
                return true;
            if (this.cancelBuyButton.mouseClicked(mouseX, mouseY, button))
                return true;
            return true; // Block other clicks
        }

        // Market List Interactions (Select Logic)
        int startX = this.leftPos + 8;
        int startY = this.topPos + 18;
        int rowHeight = 20;
        int maxRows = 3;

        java.util.List<com.daigo.marketboard.market.MarketListing> listings = ClientMarketData.getInstance()
                .getListings();

        for (int i = 0; i < Math.min(listings.size(), maxRows); i++) {
            int y = startY + (i * rowHeight);
            if (mouseX >= startX && mouseX <= startX + 160 && mouseY >= y && mouseY < y + rowHeight) {
                // Enter Buy Mode instead of sending packet immediately
                this.selectedListing = listings.get(i);
                this.isBuyMode = true;
                updateWidgetVisibility();
                net.minecraft.client.Minecraft.getInstance().player
                        .playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isSellMode) {
            if (this.priceInput.mouseReleased(mouseX, mouseY, button))
                return true;
            if (this.confirmSellButton.mouseReleased(mouseX, mouseY, button))
                return true;
            if (this.cancelSellButton.mouseReleased(mouseX, mouseY, button))
                return true;
            return true;
        }
        if (isBuyMode) {
            if (this.confirmBuyButton.mouseReleased(mouseX, mouseY, button))
                return true;
            if (this.cancelBuyButton.mouseReleased(mouseX, mouseY, button))
                return true;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isSellMode) {
            if (this.priceInput.mouseDragged(mouseX, mouseY, button, dragX, dragY))
                return true;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isSellMode) {
            if (this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
            if (keyCode == 256) { // ESC
                this.isSellMode = false;
                updateWidgetVisibility();
                return true;
            }
            return false;
        }
        if (isBuyMode) {
            if (keyCode == 256) { // ESC
                this.isBuyMode = false;
                this.selectedListing = null;
                updateWidgetVisibility();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (isSellMode) {
            if (this.getFocused() != null && this.getFocused().charTyped(codePoint, modifiers)) {
                return true;
            }
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }
}
