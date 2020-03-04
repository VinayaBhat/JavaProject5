package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.ParserException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlParserandDumper {
    Airline<Flight> a1;
     /**
     * Testing the dump functions which writes to xml file
     */
    @Test
    public void testingxmldump() throws IOException {
        StringWriter stringWriter = new StringWriter();
        stringWriter.flush();
        a1=new Airline<>("Alaska");
        Flight flight = new Flight("5");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/20/2020", "11:40 pm");
        flight.setArrival_time("1/21/2020","10:30 pm");
        a1.addFlight(flight);
        XMLDumper td=new XMLDumper(stringWriter);
        td.dump(a1);
    }

}
