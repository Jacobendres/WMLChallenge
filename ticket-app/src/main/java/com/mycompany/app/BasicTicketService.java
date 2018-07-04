package com.mycompany.app;

import java.util.*;


public class BasicTicketService implements TicketService {
    private int totalSeats;
    private int rowLength;
    private int holdId;
    private HashSet<Integer> takenSeats;//both reserved and held seats
    private HashSet<Integer> confirmedSeats;//seats that are reserved fully
    private ArrayList<SeatHold> heldSeats;//made up of SeatHold objects not individual seat #s

    BasicTicketService(int venueRows, int venueCols){
        totalSeats = venueRows * venueCols;
        rowLength = venueCols;
        takenSeats = new HashSet<>();
        confirmedSeats = new HashSet<>();
        heldSeats = new ArrayList<>();
        holdId = 0;
    }
    public int numSeatsAvailable(){
        return totalSeats - takenSeats.size();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail){
        boolean availTogether = false;
        int groupStart = 0;
        SeatHold currentHold;
        if(numSeats == 1){
            int seat = individualSeatFinder();
            currentHold = new SeatHold(holdId, true, customerEmail);
            heldSeats.add(currentHold);
            holdId++;
            currentHold.add(seat);
            takenSeats.add(seat);
            System.out.println("User selected seat reserved.");
            return currentHold;
        }
        if(numSeats<1){
            System.out.println("Cannot reserve zero or fewer seats.");
            currentHold = new SeatHold(-1, false, customerEmail);
            return currentHold;
        }
        if (numSeats > this.numSeatsAvailable()){
            System.out.println("Not enough seats left for your order.");
            currentHold = new SeatHold(-1, false, customerEmail);
            return currentHold;
        }

        if(numSeats <= rowLength) {
            for (int i = 0; i + numSeats <= totalSeats; i++) {
                for (int j = 0; j < numSeats; j++) {

                    if (takenSeats.contains((j + i))) {
                        j = numSeats;
                    }
                    else if (j == numSeats - 1 && i/rowLength == (i+j)/rowLength){
                            availTogether = true;
                        groupStart = i;
                    }

                }
                if (availTogether) {
                    currentHold = new SeatHold(holdId, true, customerEmail);
                    heldSeats.add(currentHold);
                    holdId++;
                    for (int k = 0; k < numSeats ; k++){
                       currentHold.add(k+i);
                       takenSeats.add(k+i);
                    }
                    System.out.println("Reserved seats next to each other.");
                    return currentHold;
                }

            }
        }
        currentHold = new SeatHold(holdId, false, customerEmail);
        heldSeats.add(currentHold);
        holdId++;
        for (int i = 0, t = 0; i < totalSeats && t < numSeats; i++ ){
            if (!takenSeats.contains(i)){
                currentHold.add(i);
                takenSeats.add(i);
                t++;
            }
        }
        System.out.println("Reserved scattered seats.");
        return currentHold;

    }

    public String reserveSeats(int seatHoldId, String customerEmail){
        boolean confirmed = false;
        ArrayList<SeatHold> remover =new ArrayList<>();
        for ( SeatHold hold:
                heldSeats) {
            if (seatHoldId == hold.getHoldId()){
                for (int i:
                     hold.getSeats()) {
                    confirmedSeats.add(i);
                }
                confirmed = true;
                remover.add(hold);
            }
        }
        heldSeats.removeAll(remover);
        if (confirmed) {
            return customerEmail.substring(0, 1) + Integer.toString(seatHoldId);
        }
        System.out.println("SeatHold ID not found.");
        return "FAILED";
    }

    private int individualSeatFinder(){
        Scanner reader = new Scanner(System.in);
        int choice1 = 1, choice2 = 1;
        boolean emptyRows[] = new boolean[totalSeats/rowLength];
        boolean emptySeats[] =new boolean[rowLength];
        do  {
            for(int i = 0;i<totalSeats;i++){
                if (!takenSeats.contains(i)){
                    emptyRows[i/rowLength] = true;
                }
            }
            System.out.print("The following rows have empty seats:");
            for (int i = 0; i<emptyRows.length; i++){
                if (emptyRows[i])
                    System.out.printf("%d  ", i + 1);
            }
            System.out.println("Please pick a row.");
            try {
               choice1 = reader.nextInt();
               //IllegalStateException is unlikely here but I don't know any downside to catching it.
            } catch (NoSuchElementException|IllegalStateException e){
                choice1 = 1;
            }
            if(!emptyRows[choice1-1])
                System.out.println("That is not one of the empty rows");
        }while (!emptyRows[choice1-1]);

        do {
            for(int i = 0;i<rowLength;i++){
                if (!takenSeats.contains(i+(choice1*rowLength))){
                    emptySeats[i] = true;
                }
            }
            System.out.printf("The following seats in that row are empty(1 is the leftmost seat %d is the rightmost):", rowLength);
            for (int i = 0; i<rowLength; i++){
                if (emptySeats[i])
                    System.out.printf("%d  ", i + 1);
            }
            System.out.println("Please pick a seat.");
            try {
                choice2 = reader.nextInt();
            } catch (NoSuchElementException|IllegalStateException e){
                choice2 = 1;
            }
            if(!emptyRows[choice2-1])
                System.out.println("That is not one of the empty rows");
        } while (!emptySeats[choice2-1]);
        return (choice1-1)*rowLength+(choice2-1);

    }

    public ArrayList<SeatHold> getHeldSeats(){
        return heldSeats;
    }
    public HashSet<Integer> getConfirmed(){
        return confirmedSeats;
    }
}
