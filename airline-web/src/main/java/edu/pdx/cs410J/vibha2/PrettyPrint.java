package edu.pdx.cs410J.vibha2;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PrettyPrint{
    public void sortFlightsandPrint(Airline airline) throws ParseException {
        Collection<Flight> flights=airline.getFlights();
        if(flights.size()==0){
            System.out.println("Airline "+airline.getName()+" has 0 flights !");
        }
        List<Flight> flightdata = new ArrayList<>(flights);
        Collections.sort(flightdata);
        for (Flight f : flightdata) {
            System.out.println(airline.AirlinetoString(f));
        }
    }
}

