package com.mycompany.app;

import java.util.HashSet;

public class SeatHold {
    private int holdId;
    private boolean isSequential;
    private String email;
    private HashSet<Integer> seats;

    SeatHold (int seatHoldId, boolean sequential, String customerEmail){
        holdId = seatHoldId;
        isSequential = sequential;
        email = customerEmail;
        seats = new HashSet<>();
    }

    public boolean add(int seat){
       boolean add = seats.add(seat);
       return add;
    }

    public int getHoldId(){
        return holdId;
    }

    public boolean isSequential() {
        return isSequential;
    }

    public String getEmail(){
        return email;
    }

    public HashSet<Integer>  getSeats(){
        return seats;
    }
}
