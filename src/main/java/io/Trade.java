package io;

import org.bukkit.Material;

import java.util.UUID;

public class Trade {

    private int price, amount;
    private Material item;

    private UUID buyer, seller;
    private boolean buyerReceived, sellerReceived;

    protected Trade(int amount, Material item, int price) {
        this.amount = amount;
        this.item = item;
        this.price = price;
    }

    public int getAmount() { return amount; }
    public int getPrice() { return price; }
    public Material getItem() { return item; }

    public void assignBuyer(UUID playerUUID) { buyer = playerUUID; }
    public void assignSeller(UUID playerUUID) { seller = playerUUID; }

    public void accept(UUID playerID) {
        if (buyer == null && seller != null) buyer = playerID;
        if (seller == null && buyer != null) seller = playerID;
    }

    public void markBuyerReceived() { buyerReceived = true; }
    public void markSellerReceived() { sellerReceived = true; }
    public boolean buyerReceived() { return buyerReceived; }
    public boolean sellerReceived() { return sellerReceived; }

}
