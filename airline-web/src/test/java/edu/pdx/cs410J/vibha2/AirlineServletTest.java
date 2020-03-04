package edu.pdx.cs410J.vibha2;

import org.junit.Test;
import org.junit.runner.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AirlineServletTest {
  /**
   * Test doGet method with no Airline stored
   * @throws ServletException
   * @throws IOException
   */
  @Test
  public void initiallyServletContainsNoAirline() throws ServletException, IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request, response);

    Map<String,String[]> dummy=new HashMap<>();
    dummy.put("a",new String[]{"b"});
    dummy.put("a1",new String[]{"b"});
    dummy.put("a2",new String[]{"b"});
    dummy.put("a3",new String[]{"b"});

    when(request.getParameterMap()).thenReturn(dummy);
    servlet.doGet(request,response);

    Map<String,String[]> dummy1=new HashMap<>();
    dummy1.put("name",new String[]{"Alaska"});
    when(request.getParameterMap()).thenReturn(dummy1);
    when(request.getParameter("name")).thenReturn("Alaska");
    servlet.doGet(request,response);

    Map<String,String[]> dummy2=new HashMap<>();
    dummy1.put("name",new String[]{"Alaska"});
    Map<String,Airline> airlineMap=new HashMap<>();
    Airline<Flight> airline=new Airline<>("Alaska");
    Flight flight=new Flight("123");
    flight.setSource("JFK");
    flight.setDestination("PDX");
    flight.setDeparture_time("1/3/2020","10:30 pm");
    flight.setArrival_time("1/21/2020","10:30 pm");

    airlineMap.put("Alaska",airline);
    when(request.getParameterMap()).thenReturn(dummy2);
    when(request.getParameter("name")).thenReturn("Alaska");
    servlet.doGet(request,response);

  }

  /**
   * Test with doPost method
   * @throws ServletException
   * @throws IOException
   */
  @Test
  public void testingdoPost() throws ServletException, IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request,response);

    when(request.getParameter("airline")).thenReturn("Alaska");
    servlet.doPost(request,response);
    when(request.getParameter("flightnumber")).thenReturn("123");
    servlet.doPost(request,response);
    when(request.getParameter("src")).thenReturn("JFK");
    servlet.doPost(request,response);
    when(request.getParameter("depart")).thenReturn("1/2/2020 10:30 pm");
    servlet.doPost(request,response);
    when(request.getParameter("dest")).thenReturn("PDX");
    servlet.doPost(request,response);
    when(request.getParameter("arrival")).thenReturn("1/3/2020 10:30 pm");
    servlet.doPost(request,response);

    servlet.doPost(request,response);

    servlet.doDelete(request,response);
  }

  /**
   * Test with doGet method with airline name not found
   * @throws IOException
   * @throws ServletException
   */
  @Test
  public void doGettest() throws IOException, ServletException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter("airline")).thenReturn("Alaska");
    when(request.getParameter("flightnumber")).thenReturn("123");
    when(request.getParameter("src")).thenReturn("JFK");
    when(request.getParameter("depart")).thenReturn("1/2/2020 10:30 pm");
    when(request.getParameter("dest")).thenReturn("PDX");
    when(request.getParameter("arrival")).thenReturn("1/3/2020 10:30 pm");
    servlet.doPost(request,response);

    HttpServletRequest request1 = mock(HttpServletRequest.class);
    HttpServletResponse response1 = mock(HttpServletResponse.class);
    PrintWriter pw1 = mock(PrintWriter.class);
    when(response1.getWriter()).thenReturn(pw1);
    when(request1.getParameter("name")).thenReturn("Emirates");
    servlet.doGet(request1,response1);
  }

  /**
   * Test with doGet method with Airline name present
   * @throws IOException
   * @throws ServletException
   */
  @Test
  public void doGettEST2() throws IOException, ServletException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter("airline")).thenReturn("Alaska");
    when(request.getParameter("flightnumber")).thenReturn("123");
    when(request.getParameter("src")).thenReturn("JFK");
    when(request.getParameter("depart")).thenReturn("1/2/2020 10:30 pm");
    when(request.getParameter("dest")).thenReturn("PDX");
    when(request.getParameter("arrival")).thenReturn("1/3/2020 10:30 pm");
    servlet.doPost(request,response);

    HttpServletRequest request1 = mock(HttpServletRequest.class);
    HttpServletResponse response1 = mock(HttpServletResponse.class);
    PrintWriter pw1 = mock(PrintWriter.class);
    when(response1.getWriter()).thenReturn(pw1);
    when(request1.getParameter("name")).thenReturn("Alaska");
    servlet.doGet(request1,response1);
    when(request1.getParameter("src")).thenReturn("JFK");
    servlet.doGet(request1,response1);
    when(request1.getParameter("src")).thenReturn("JFK");
    when(request1.getParameter("dest")).thenReturn("PDX");
    servlet.doGet(request1,response1);


  }


}
