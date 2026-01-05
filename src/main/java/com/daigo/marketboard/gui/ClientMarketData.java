package com.daigo.marketboard.gui;

import com.daigo.marketboard.market.MarketListing;
import java.util.ArrayList;
import java.util.List;

public class ClientMarketData {
    private static final ClientMarketData INSTANCE = new ClientMarketData();
    private List<MarketListing> clientListings = new ArrayList<>();

    private ClientMarketData() {
    }

    public static ClientMarketData getInstance() {
        return INSTANCE;
    }

    public void updateListings(List<MarketListing> newListings) {
        this.clientListings = new ArrayList<>(newListings);
    }

    public List<MarketListing> getListings() {
        return clientListings;
    }
}
