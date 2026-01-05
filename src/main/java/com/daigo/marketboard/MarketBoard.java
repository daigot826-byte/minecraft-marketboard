package com.daigo.marketboard;

import com.daigo.marketboard.gui.MarketBoardMenu;
import net.minecraft.world.inventory.MenuType;

import com.daigo.marketboard.money.MoneyAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.BuiltInRegistries;

@Mod(MarketBoard.MODID)
public class MarketBoard {
        public static final String MODID = "marketboard";

        public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
                        .create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

        public static final DeferredHolder<AttachmentType<?>, AttachmentType<MoneyAttachment>> MONEY = ATTACHMENT_TYPES
                        .register(
                                        "money",
                                        () -> AttachmentType.builder(() -> new MoneyAttachment())
                                                        .serialize(MoneyAttachment.CODEC)
                                                        .copyOnDeath().build());

        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
                        MODID);

        public static final DeferredHolder<MenuType<?>, MenuType<MarketBoardMenu>> MARKET_MENU = MENUS.register(
                        "market_menu",
                        () -> IMenuTypeExtension.create((windowId, inv, data) -> new MarketBoardMenu(windowId, inv)));

        public MarketBoard(IEventBus modEventBus) {
                ATTACHMENT_TYPES.register(modEventBus);
                MENUS.register(modEventBus);
        }
}
