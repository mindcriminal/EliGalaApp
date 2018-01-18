package com.mindcriminal.eligalaapp;

/**
 * Created by mindcriminal on 10/31/17.
 */

public class RaffleData {

    public int id;
    public String name;
    public Integer ticket;
    public Integer prize;

    public RaffleData()
    {}

    public RaffleData(String n, Integer t, Integer p)
    {
        this.name=n;
        this.ticket=t;
        this.prize=p;
    }

    public void setData(String n, Integer t, Integer p)
    {
        this.name=n;
        this.ticket=t;
        this.prize=p;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTicket() {
        return ticket;
    }
    public void setTicket(int ticket) {
        this.ticket = ticket;
    }
    public int getPrize() {
        return prize;
    }
    public void setPrize(int prize) {
        this.prize = prize;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RaffleTicket [id=" + id + ", name=" + name + ", ticket=" + ticket + ", prize=" + prize
                + "]";
    }

}
