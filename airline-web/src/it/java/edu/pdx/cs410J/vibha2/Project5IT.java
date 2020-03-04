package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.vibha2.AirlineRestClient.AirlineRestException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    /**
     *Testing the airline post method
     * @throws IOException
     */
    @Test
    public void populateFlightTest() throws IOException {
      AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
        Airline airline=new Airline<>("Alaska");
        Flight flight=new Flight("123");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/3/2020","10:30 pm");
        flight.setArrival_time("1/21/2020","10:30 pm");
      client.populateFlight(airline);
    }

    /**
     * Test for no cmd line arguments
     */
    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project5.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_ARGUMENTS));
    }

    /**
     * Test for port and host of server
     */
    @Test
    public void portandHost() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT );
    }

    /**
     * No airline: search with Airline name
     */
    @Test
    public void test2EmptyServersearch() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT,"-search","Alaska" );
    }

    /**
     * No airline name: search test
     */
    @Test
    public void test2EmptyServersearch2() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT,"-search");

    }

    /**
     *  No airline: search with Airline name src dest
     */
    @Test
    public void test2EmptyServersearch3() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT,"-search","Alaska","JFK","PDX");
    }

    /**
     * Test of createFlight with less arguments
     */
    @Test
    public void createFlight() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT,"Alaska","123","JFK","1/2/2020 10:30 pm","PDX","2/2/2020 10:30 pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Unable to connect to server: localhost:8080 Message Less arguments to create Flight"));
    }

    /**
     * Test of createFlight
     */
    @Test
    public void createFlight2() {
        MainMethodResult result = invokeMain( Project5.class,"-host", HOSTNAME, "-port",PORT,"Alaska","123","JFK","1/2/2020","10:30", "pm","PDX","2/2/2020","10:30", "pm");
    }

    /**
     * Read me test
     */
    @Test
    public void readMeTest() {
        MainMethodResult result = invokeMain(Project5.class, "-README");
    }

    /**
     * Test with host name null
     */
    @Test
    public void hostNameNull() {
        MainMethodResult result = invokeMain(Project5.class, "-host");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("MISSING HOSTNAME ARGUMENT"));
    }

    /**
     * Test with port name not int
     */
    @Test
    public void portNotString() {
        MainMethodResult result = invokeMain(Project5.class, "-host","localhost","-port","HI");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("PORT HI MUST BE AN INT"));
    }

    /**
     * Test with missing port
     */
    @Test
    public void missingPort() {
        MainMethodResult result = invokeMain(Project5.class, "-host","localhost","-port");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("MISSING PORT ARGUMENT"));
    }

    /**
     * Test with -print and create
     */
    @Test
    public void printandCreateFlight() {
        MainMethodResult result = invokeMain( Project5.class,"-print","-host", HOSTNAME, "-port",PORT,"Alaska","123","JFK","1/2/2020","10:30", "pm","PDX","2/2/2020","10:30", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Airline Name: Alaska with Flight 123 departs JFK at 1/2/20 10:30 PM arrives PDX at 2/2/20 10:30 PM"));
    }

    /**
     * Test with Create Flight with extra arguments
     */
    @Test
    public void printandCreateFlightExtraArgs() {
        MainMethodResult result = invokeMain( Project5.class,"-print","-host", HOSTNAME, "-port",PORT,"Alaska","123","JFK","1/2/2020","10:30", "pm","PDX","2/2/2020","10:30", "pm","hi");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Unable to connect to server: "+HOSTNAME+":"+PORT+" Message Extra arguments to create Flight"));
    }

}