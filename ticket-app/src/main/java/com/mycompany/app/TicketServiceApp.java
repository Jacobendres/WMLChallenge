package com.mycompany.app;

import java.util.Scanner;

public class TicketServiceApp {
    public static void main(String args[]){
        boolean cont = true;
        int seat;
        String y, e;
        Scanner reader = new Scanner(System.in);
        System.out.println("How many rows in your venue?");
        int row = reader.nextInt();
        System.out.println("How many seats per row in your venue?");
        int col = reader.nextInt();
        BasicTicketService bst = new BasicTicketService(row,col);
        while (cont){
            System.out.println("How many seats do you want?");
            seat= reader.nextInt();
            System.out.println("What is your email?");
            e = reader.next();
            bst.findAndHoldSeats(seat, e );
            System.out.println("Do you want to continue? y/n");
            y = reader.next();

            if(!(y.substring(0,1).toLowerCase().equals("y")))
                cont = false;

        }
    }
}
