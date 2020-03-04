package edu.pdx.cs410J.vibha2;

import org.junit.Test;

import java.text.ParseException;

public class PrettyPrintTest {
    @Test
    public void PrettyPrinttest() throws ParseException {
        Flight flight = new Flight("5");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/20/2020", "11:30 am");
        flight.setArrival_time("1/21/2020", "10:30 am");
        Airline airline=new Airline("Alaska");
        airline.addFlight(flight);
        PrettyPrint pp=new PrettyPrint();
        pp.sortFlightsandPrint(airline);
    }
}
