package edu.pdx.cs410J.vibha2;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
  public Map<String,Airline> airlineMap=new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * word specified in the "word" HTTP parameter to the HTTP response.  If the
   * "word" parameter is not specified, all of the entries in the dictionary
   * are written to the HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
      response.setContentType( "text/plain" );
      if(request.getParameterMap().size()>3){
          String message = "Extra parameters found in GET request!";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      }
      String airlineName = getParameter( "name", request );
      if (airlineName == null) {
          missingRequiredParameter(response, "name");
          return;
      }
      String src = getParameter( "src", request );
      String dest = getParameter( "dest", request );
      if(airlineMap.size()==0){
          String message = "No airline found!";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      }else if(!airlineMap.containsKey(airlineName)){
          String message = "No airline by the name :"+airlineName+" found!";
          response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
      }else if(airlineMap.containsKey(airlineName)){
          Airline airline=airlineMap.get(airlineName);
          StringWriter stringWriter = new StringWriter();
          stringWriter.flush();
          XMLDumper xd =new XMLDumper(stringWriter);
          if(src==null && dest==null){
              xd.dump(airline);
              response.getWriter().println(xd.XMLDumperWriter());
              response.setStatus(HttpServletResponse.SC_OK);
              Collection<Flight> f=airline.getFlights();
              for(Flight flight:f){
                  try {
                      System.out.println(airline.AirlinetoString(flight));
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
          }else if(src!=null && dest!=null){
              Airline newairline=new Airline(airlineName);
              Collection<Flight> f=airline.getFlights();
              for(Flight fligthinfo:f) {
                  if(fligthinfo.getSource().equals(src)&& fligthinfo.getDestination().equals(dest)){
                      newairline.addFlight(fligthinfo);
                  }
              }
              xd.dump(newairline);
              response.getWriter().println(xd.XMLDumperWriter());
              response.setStatus(HttpServletResponse.SC_OK);
              Collection<Flight> f1=newairline.getFlights();
              for(Flight flight:f1){
                  try {
                      System.out.println(newairline.AirlinetoString(flight));
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
          }else{
              String message = "src or dest is not found in parameter";
              response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
          }

      }

  }

  /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
      response.setContentType( "text/plain" );
      String airlinename = getParameter("airline", request );
      if (airlinename == null) {
          missingRequiredParameter(response, "airline");
          return;
      }
      String flightnum = getParameter("flightnumber", request );
      if ( flightnum == null) {
          missingRequiredParameter( response, "flightnumber" );
          return;
      }
      String src = getParameter("src", request );
      if ( src == null) {
          missingRequiredParameter( response, "src" );
          return;
      }
      String departstr = getParameter("depart", request );
      if ( departstr == null) {
          missingRequiredParameter(response, "depart");
          return;
      }

      String dest = getParameter("dest", request );
      if ( dest == null) {
          missingRequiredParameter( response, "dest" );
          return;
      }
      String arrivalstr = getParameter("arrival", request );
      if ( arrivalstr == null) {
          missingRequiredParameter( response, "arrival" );
          return;
      }

      Airline airlineobj;
      if(airlineMap.containsKey(airlinename)){
          airlineobj=airlineMap.get(airlinename);
      }else{
          airlineobj=new Airline(airlinename);
      }
      Flight flightobj=new Flight(flightnum);
      flightobj.setSource(src);
      flightobj.setDestination(dest);
      String[] depart=departstr.split(" ");
      flightobj.setDeparture_time(depart[0],depart[1]+" "+depart[2]);
      String[] arrival=arrivalstr.split(" ");
      flightobj.setArrival_time(arrival[0],arrival[1]+" "+arrival[2]);
      airlineobj.addFlight(flightobj);
      airlineMap.put(airlinename,airlineobj);

      response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/plain");

      this.airlineMap.clear();

      PrintWriter pw = response.getWriter();
      pw.println("All airlines deleted");
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }



  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }


}
