package com.mindcriminal.eligalaapp;

/**
 * Created by mindcriminal on 10/31/17.
 */

public class RaffleData {
    private String name;
    private String ticket;
    private String prize;

    public RaffleData()
    {}

    public RaffleData(String n, String t, String p)
    {
        this.name=n;
        this.ticket=t;
        this.prize=p;
    }

    public void setData(String n, String t, String p)
    {
        this.name=n;
        this.ticket=t;
        this.prize=p;
    }
}
