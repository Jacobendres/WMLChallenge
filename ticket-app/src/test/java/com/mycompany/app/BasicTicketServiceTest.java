package com.mycompany.app;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



public class BasicTicketServiceTest  {
    BasicTicketService bst;
    private ByteArrayInputStream testIn;
    private final InputStream systemIn = System.in;

    @Before
    public void setUp(){
        bst = new BasicTicketService(10, 10);
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    @Test
    public void countingSeatsTest(){
        assertEquals(100, bst.numSeatsAvailable());
        bst.findAndHoldSeats(1,"someemail");
        assertEquals(99,bst.numSeatsAvailable());
    }

    @Test
    public void holdingSeatsTest(){
        SeatHold hold = bst.findAndHoldSeats(2, "someemail");
        assertEquals(0, hold.getHoldId());
        assertEquals("someemail",hold.getEmail());
        assertEquals(2,hold.getSeats().size());
        assertTrue(hold.getSeats().contains(0));
        hold = bst.findAndHoldSeats(999, "someemail");
        assertEquals(-1, hold.getHoldId());
        hold = bst.findAndHoldSeats(10, "anotheremail");
        assertTrue(hold.isSequential());
        assertEquals(hold.getHoldId(), 1);
        assertEquals(hold.getSeats().size(), 10);
        assertTrue(hold.getSeats().contains(10));
        assertTrue(hold.getSeats().contains(19));
        hold = bst.findAndHoldSeats(11, "thirdemail");
        assertFalse(hold.isSequential());
        assertEquals(hold.getSeats().size(), 11);
        assertTrue(hold.getSeats().contains(2));
        assertTrue(hold.getSeats().contains(9));
        assertTrue(hold.getSeats().contains(20));
    }



    @Test
    public void reservingSeatTest(){
        String fail = bst.reserveSeats(1,"someemail");
        assertEquals("FAILED", fail);
        SeatHold hold = bst.findAndHoldSeats(5, "someemail");
        String ccode = bst.reserveSeats(0,"anotheremail");
        assertEquals("a0", ccode);
        assertEquals(0,bst.getHeldSeats().size());
        assertEquals(5, bst.getConfirmed().size());
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}