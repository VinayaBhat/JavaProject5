package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AbstractFlight;

import java.io.PrintStream;
import java.util.*;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {
    private static final ArrayList<String> AIRLINE_PARAMS;
    private static final ArrayList<String> AIRLINE_DESCRIPTOR;
    private static final ArrayList<String> OPTION_PARAMS;
    private static final ArrayList<String> OPTION_DESCRIPTIONS;


    static {
        AIRLINE_PARAMS = new ArrayList<String>(Arrays.asList(
                "name",
                "flightNumber",
                "src",
                "departureTime",
                "dest",
                "arriveTime")
        );
        AIRLINE_DESCRIPTOR = new ArrayList<String>(Arrays.asList(
                "Then name of the airline",
                "The flight number",
                "Three-letter code of departure airport",
                "Departure data and time (mm/dd/yyyy hh:mm AM/PM)",
                "Three-letter code of arrival airport",
                "Arrival date and time (mm/dd/yyyy hh:mm AM/PM)")
        );
        OPTION_DESCRIPTIONS = new ArrayList<String>(Arrays.asList(
                "Host computer on which the server is running",
                "Port on which the server is listening",
                "Search for flights",
                "Prints a description of the new flight",
                "Prints a README for this project and exits")
        );
        OPTION_PARAMS = new ArrayList<String>(Arrays.asList(
                "-host hostname",
                "-port port",
                "-search airline <source destination>",
                "-print",
                "-README")
        );
    }


     static  String hostName;
     static  String portstr;
     static boolean readMe;

     static boolean search;
     static String searchSrc;
     static String searchDest;
     static boolean createFlight;
     static boolean prettyprint;

     static String airlineName;
     static List<String> inputargs=new ArrayList<>();


    public static void main(String... args) {
        prettyprint = false;
        readMe = false;
        search = false;
        createFlight = false;
        if(args.length==0){
            System.err.println("MISSING COMMAND LINE ARGUMENTS");
            System.exit(1);
        }
        parseArguments(args);
        if(readMe)
            readMe();
        if (hostName == null){
            System.err.println("MISSING HOSTNAME ARGUMENT");
            System.exit(1);
        }
        if (portstr == null){
            System.err.println("MISSING PORT ARGUMENT");
            System.exit(1);
        }
        if(hostName != null && portstr != null) {
            int port = 0;
            try {
                port = Integer.parseInt(portstr);
            } catch (Exception e) {
                System.err.println("PORT "+portstr+"MUST BE AN INT");
                System.exit(1);
            }
            AirlineRestClient client = new AirlineRestClient(hostName, port);
            try{
                if(prettyprint && !createFlight){
                    System.err.println("-print cannot be specified without new flight creation");
                    System.exit(1);
                }
                if(search)
                {
                    if(searchSrc == null || searchDest == null) {
                       String output = client.getAirlineInfo(airlineName);
                    }
                    else {
                        String output = client.getAirlineInfoSRCDEST(airlineName,searchSrc,searchDest);
                    }
                }else if(createFlight){
                    if(inputargs.size()==10){
                        Airline airline=new Airline(inputargs.get(0));
                        Flight flight=new Flight(inputargs.get(1));
                        flight.setSource(inputargs.get(2));
                        flight.setDeparture_time(inputargs.get(3),inputargs.get(4)+" "+inputargs.get(5));
                        flight.setDestination(inputargs.get(6));
                        flight.setArrival_time(inputargs.get(7),inputargs.get(8)+" "+inputargs.get(9));
                        airline.addFlight(flight);
                        client.populateFlight(airline);
                        if(prettyprint){
                            System.out.println("Airline Name: "+airlineName+" with "+flight.toString());
                        }
                    }else if(inputargs.size()<10){
                        System.err.println("Less arguments to create Flight");
                        System.exit(1);
                    }else{
                        System.err.println("Extra arguments to create Flight");
                        System.exit(1);
                    }

                }

            }catch (Exception e){
                System.err.println("Unable to connect to server: " + hostName + ":" + port +" Message "+e.getMessage());
                System.exit(1);
            }
        }




    }

    private static void readMe() {
        System.out.println("Project 5: Airline Web");
        System.out.println("Author: Vinaya D Bhat");
        System.out.println("This project extends the airline application to support an airline server that provides REST-ful\n" +
                "web services to an airline client");
        System.out.println("It supports GET/POST/DELETE HTTP methods");
        System.out.println("This application is a web application which stores the details of the flight on server.");
        System.out.println("The project takes options : -host hostname -port port -search (airlinename,(src,dest)) -print -README");
        System.out.println("The project takes args: airlinename flightnum src departuretime dest arrivaltime");
        System.exit(0);
    }

    private static void  parseArguments(String[] args) {
        List<String> list = Arrays.asList(args);
        Iterator<String> listiterator = list.iterator();
        String param = null;
        while(listiterator.hasNext()){
             param=listiterator.next();
            if(param.equals("-search")){
                search=true;
            }else  if(param.equals("-print")){
                prettyprint=true;
            }else if(param.equals("-README")) {
                readMe = true;
            }else if(param.equals("-port")){
                portstr=listiterator.next();
            }else if(param.equals("-host")){
                hostName=listiterator.next();
            }else{
                break;
            }
        }
        List<String> newList=new ArrayList<>();
        newList.add(param);
        while(listiterator.hasNext()){
            newList.add(listiterator.next());
        }

       if(search){
           if(newList.size()==0){
               System.err.println("ZERO arguments found in -search");
               System.exit(1);
           }else if(newList.size()==1){
               airlineName=newList.get(0);
           }else if(newList.size()==3){
               airlineName=newList.get(0);
               searchSrc=newList.get(1);
               searchDest=newList.get(2);
           }else if(newList.size()==2){
               System.err.println("Less arguments found in -search");
               System.exit(1);
           }else{
               System.err.println("Extra arguments found in -search");
               System.exit(1);
           }
       }else if(newList.size()>1) {
           createFlight=true;
           airlineName=newList.get(0);
           inputargs=newList;
       }
    }

}