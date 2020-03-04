package edu.pdx.cs410J.vibha2;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.family.PrettyPrinter;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.net.HttpURLConnection.HTTP_OK;



/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient extends HttpRequestHelper
{
  private static final String WEB_APP = "airline";
  private static final String SERVLET = "flights";

  private final String url;



  /**
   * Creates a client to the airline REST service running on the given host and port
   * @param hostName The name of the host
   * @param port The port
   */
  public AirlineRestClient( String hostName, int port )
  {
    this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
  }

  /**
   * Returns all flights from given airline
   */
  public String getAirlineInfo(String airlineName) throws IOException, ParserException, SAXException, ParserConfigurationException, ParseException {
    Map<String,String> prop=new HashMap<>();
    prop.put("name",airlineName);
    Response response = get(this.url, prop);
    String content = response.getContent();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(content));
    XMLParser parser = new XMLParser(builder.parse(is));
    Airline airline = parser.parse();
    PrettyPrint pp=new PrettyPrint();
    pp.sortFlightsandPrint(airline);
    throwExceptionIfNotOkayHttpStatus(response,content);
    return content;
  }

  /**
   * Returns all flights from given airline given src and dest
   */
  public String getAirlineInfoSRCDEST(String airlineName,String src,String dest) throws IOException, SAXException, ParseException, ParserException, ParserConfigurationException {
    Map<String,String> prop=new HashMap<>();
    prop.put("name",airlineName);
    prop.put("src",src);
    prop.put("dest",dest);
    Response response = get(this.url, prop);
    String content = response.getContent();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(content));

    XMLParser parser = new XMLParser(builder.parse(is));
    Airline airline = parser.parse();
   PrettyPrint pp=new PrettyPrint();
   pp.sortFlightsandPrint(airline);
    throwExceptionIfNotOkayHttpStatus(response,content);
    return content;
  }


  @VisibleForTesting
  Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {

    return post(this.url, dictionaryEntries);
  }

  private Response throwExceptionIfNotOkayHttpStatus(Response response,String content) {
    int code = response.getCode();
    if (code != HTTP_OK) {
      throw new AirlineRestException(code,content);
    }
    return response;
  }

  @VisibleForTesting
  class AirlineRestException extends RuntimeException {
    AirlineRestException(int httpStatusCode,String content) {
      super("Got an HTTP Status Code of " + httpStatusCode+ " MESSAGE: "+content.toUpperCase());
    }
  }

  public void populateFlight(Airline<AbstractFlight> airline) {
    try {
      Collection<Flight> f = airline.getFlights();
      for (Flight flight : f) {
        Response response = postToMyURL(Map.of(
                "airline", airline.getName(),
                "flightnumber", String.valueOf(flight.getNumber()),
                "src", flight.getSource(),
                "depart", flight.getDepartureString(),
                "dest", flight.getDestination(),
                "arrival", flight.getArrivalString()));
        throwExceptionIfNotOkayHttpStatus(response,response.getContent());
      }
    } catch (Exception e) {
      System.err.println("Error while Adding Flight " + e.getMessage());
      System.exit(1);
    }
  }
}

