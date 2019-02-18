package io;

import org.bukkit.Material;

import java.util.UUID;

public class Trade {

    private int price, amount;
    private boolean buy;
    private Material item;

    private UUID poster, buyer;
    private boolean paid, transferCompleted;

    protected Trade(boolean buy, int amount, Material item, int price, UUID playerUUID) {
        this.buy = buy;
        this.amount = amount;
        this.item = item;
        this.price = price;
    }

    public int getAmount() { return amount; }
    public int getPrice() { return price; }
    public Material getItem() { return item; }
    public boolean isBuyOffer() { return buy; }

    public void accept(PlayerData acceptor) {

    }

}
