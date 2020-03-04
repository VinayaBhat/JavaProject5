package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Flight is a class which extends AbstractFlight.
 */
public class Flight extends AbstractFlight implements Comparable<Flight> {
    //properties

    int flight_number;
    String source;
    String destination;
    String arrival_time;
    String departure_time;



    //constructor

    /**
     * Flight constructor allows us to initialize the Flight object. The flight constructor calls the flightnumberisvalid
     * function to check if the flightnumber is valid integer.
     *
     * @param flightNumber is String
     */
    public Flight(String flightNumber) {
        if (flightnumberisvalid(flightNumber)) {
            this.flight_number = Integer.parseInt(flightNumber);
        } else {
            throw new IllegalArgumentException("Flight number is not set properly");
        }
    }

    //Setters

    /**
     * setSource is a setter function to set the flight source property.
     *
     * @param source is a String
     */
    public void setSource(String source) {
        if (flightsrcdestisvalid(source)) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Source airport code is invalid");

        }
    }

    /**
     * setDestination is a setter function to set the flight destination property.
     *
     * @param destination is a String
     */
    public void setDestination(String destination) {
        if (flightsrcdestisvalid(destination)) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destination airport code not valid");

        }
    }

    /**
     * setArrival_time is a setter function to set the arrival date and time of the flight.date is in the format MM/dd/yyy and time HH:mm.
     *
     * @param arrivalDate is a String
     * @param arrivalTime is a String.
     */
    public void setArrival_time(String arrivalDate, String arrivalTime) {
        try {
            if (checkdateandtime("Arrival", arrivalDate, arrivalTime)) {
                if (checkarranddepdate( this.departure_time,arrivalDate + " " + arrivalTime)) {
                    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date depdate = sdf.parse(arrivalDate);
                    String date=DateFormat.getDateInstance(DateFormat.SHORT).format(depdate);
                    DateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                    Date deptime = sdf1.parse(arrivalTime);
                    String time=DateFormat.getTimeInstance(DateFormat.SHORT).format(deptime);
                    this.arrival_time = date+" "+time;
                } else {
                    throw new Exception(" Departure Time is not less than arrival time");
                }
            }
        }catch (Exception e){
            System.err.println("Error while checking setting arrival time "+e.getMessage());
            System.exit(1);
        }
    }

    /**
     * setDeparture_time is a setter function to set the departure date and time of the flight.date is in the format MM/dd/yyy and time HH:mm.
     *
     * @param departureDate is a String
     * @param departureTime is a String
     */
    public void setDeparture_time(String departureDate, String departureTime){
        try {
            if (checkdateandtime("Departure", departureDate, departureTime)) {
                DateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                Date depdate = sdf.parse(departureDate);
                String date=DateFormat.getDateInstance(DateFormat.SHORT).format(depdate);
                DateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                Date deptime = sdf1.parse(departureTime);
                String time=DateFormat.getTimeInstance(DateFormat.SHORT).format(deptime);
                this.departure_time = date+" "+time;
            }
        }catch (Exception e){
            System.err.println("Departure Date not set properly");
            System.exit(1);
        }

    }

    /**
     * getNumber is a getter function inherited from AbstractFlight.
     *
     * @return int flightnumber.
     */
    @Override
    public int getNumber() {
        return this.flight_number;
    }

    /**
     * getSource is a getter function inherited from AbstractFlight.
     *
     * @return String source
     * @throws UnsupportedOperationException if the flight source is null
     */
    @Override
    public String getSource() {
        if (source == null) {
            throw new UnsupportedOperationException("Source not set");

        }
        return this.source;
    }

    /**
     * getDepartureString is a getter function inherited from Abstract flight.
     *
     * @return String departure date datetime
     * @throws UnsupportedOperationException if the departure date and time is null
     */
    @Override
    public String getDepartureString() {
        if (departure_time == null) {
            throw new UnsupportedOperationException("Departure Date and Time not set");
        }
        return this.departure_time;
    }

    /**
     * getDestination is a getter method inherited from the AbstractFlight.
     *
     * @return String destination
     * @throws UnsupportedOperationException is destination is null
     */
    @Override
    public String getDestination() {
        if (destination == null) {
            throw new UnsupportedOperationException("Destination not set");

        }
        return this.destination;
    }

    /**
     * getArrivalString is a getter method inherited from AbstractFlight.
     *
     * @return String arrival datetime
     * @throws UnsupportedOperationException if arrival date or time is null
     */
    @Override
    public String getArrivalString() {
        if (arrival_time == null) {
            throw new UnsupportedOperationException("Arrival Date and Time not set");

        }
        return this.arrival_time;
    }

    /**
     * flightsrcdestisvalid checks if the source or destination String is a 3 letter word and contains only alphabets.
     *
     * @param flightinfo is the source or destination string
     * @return boolean
     */
    public static boolean flightsrcdestisvalid(String flightinfo) {
        String airportname=AirportNames.getName(flightinfo);
        if (airportname==null || airportname.length()==0) {
            return false;
        }
        return true;
    }

    /**
     * flightnumberisvalid checks if the flight number contains only numeric characters.
     *
     * @param flightnumber is the flight number given during Flight initialization
     * @return boolean
     */
    public static boolean flightnumberisvalid(String flightnumber) {
        String regex = "[0-9]+";
        return flightnumber.matches(regex);
    }

    /**
     * checkdateandtime checks whether the date is in the format MM/dd/yyy and time HH:mm.
     *
     * @param sd   String can either be "arrival" or "departure"
     * @param date in the format MM/dd/yyy
     * @param time in the format HH:mm
     * @return boolean
     * @throws IllegalArgumentException if date and time are not proper
     */
    public static boolean checkdateandtime(String sd, String date, String time) {

        String dateregex = "^(1[0-2]|0[1-9]|[1-9])/(3[01]|[12][0-9]|0[1-9]|[1-9])/([0-9]{4}|[0-9]{2})$";
        Pattern pattern1 = Pattern.compile(dateregex);
        Matcher matcher1 = pattern1.matcher(date);
        boolean correctdate = matcher1.matches();
        if (!correctdate) {
            throw new IllegalArgumentException(sd + " date not set properly");
        }
        try {
            DateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date timepls = sdf.parse(time);

        } catch (Exception e) {
            throw new IllegalArgumentException(sd + " time not set properly");
        }
        return true;
    }

    /**
     * checkarranddepdate checks if the arrival date > departure date
     * @param departuredate is a string
     * @param arrivaldate is a string
     * @return boolean
     */

    public static boolean checkarranddepdate(String departuredate,String arrivaldate){

        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
            Date dep = sdf.parse(departuredate);
            Date arr = sdf.parse(arrivaldate);
            if (dep.compareTo(arr) < 0) {
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error while checking if departure time < arrival time "+e.getMessage());
            System.exit(1);
        }

        return false;
    }


    /**
     * compareTo compares flight source and departure time
     * @param t1 Flight
     * @return 0,-1,1
     */
    @Override
    public int compareTo(Flight t1) {
        int value1 = this.getSource().compareTo(t1.getSource());
        if (value1 == 0) {
            int value2 = this.getDepartureString().compareTo(t1.getDepartureString());
            return value2;
        }
        return value1;
    }

    /**
     * Caluclte the duration of flight in minutes
     * @param dep departure date and time
     * @param arr arrival date and time
     * @throws ParseException catch in checkarranddepdate method
     */
    public static long findoutminutes(String dep,String arr) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
        Date depdate = sdf.parse(dep);
        Date arrdate=sdf.parse(arr);
        long diff=arrdate.getTime()-depdate.getTime();
        return diff / (60 * 1000);
    }

}
