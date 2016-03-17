package com.doctor.sun.entity;

/**
 * Created by rick on 12/9/15.
 */
public class Fee {

    /**
     * money : 100
     * second_money : 0
     */

    private int money;
    private int second_money;
    private int commission;

    @Override
    public String toString() {
        return "Fee{" +
                "money=" + money +
                ", second_money=" + second_money +
                ", commission=" + commission +
                '}';
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setSecond_money(int second_money) {
        this.second_money = second_money;
    }

    public int getMoney() {
        return money;
    }

    public int getSecond_money() {
        return second_money;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

}
