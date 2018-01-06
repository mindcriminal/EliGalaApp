package com.mindcriminal.eligalaapp;

/**
 * Created by mindcriminal on 10/31/17.
 */

public class RaffleData {
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
}
