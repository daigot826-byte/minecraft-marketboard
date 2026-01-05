package com.daigo.marketboard.money;

import com.mojang.serialization.Codec;

public class MoneyAttachment {
    private int amount;

    public MoneyAttachment() {
        this.amount = 0;
    }

    public MoneyAttachment(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void add(int value) {
        this.amount += value;
    }

    public boolean remove(int value) {
        if (this.amount >= value) {
            this.amount -= value;
            return true;
        }
        return false;
    }

    public static final Codec<MoneyAttachment> CODEC = Codec.INT.xmap(MoneyAttachment::new, MoneyAttachment::getAmount);
}
