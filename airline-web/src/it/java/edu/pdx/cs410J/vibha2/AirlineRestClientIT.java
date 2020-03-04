package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

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

 
}
