package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.ParserException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;


public class AirlineClient {
    final String HOSTNAME = "localhost";
    final String PORT = System.getProperty("http.port", "8080");

    /**
     * Test for populateAirline method which posts new Airline
     */
    @Test
    public void populateFlightTest(){
        AirlineRestClient ar=new AirlineRestClient(HOSTNAME,Integer.parseInt(PORT));
        Flight flight = new Flight("5");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/20/2020", "11:30 am");
        flight.setArrival_time("1/21/2020", "10:30 am");
        Airline airline=new Airline("Alaska");
        airline.addFlight(flight);
        ar.populateFlight(airline);

    }

    @Test(expected = AirlineRestClient.AirlineRestException.class)
    public void testingException() {
      AirlineRestClient ar=new AirlineRestClient(HOSTNAME,Integer.parseInt(PORT));
        try {
            ar.getAirlineInfo("What");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
