package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AbstractFlight;

import java.io.PrintStream;
import java.util.*;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {
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

     static String MISSING_ARGUMENTS="MISSING COMMAND LINE ARGUMENTS";


    public static void main(String... args) {
        prettyprint = false;
        readMe = false;
        search = false;
        createFlight = false;
        if(args.length==0){
            System.err.println(MISSING_ARGUMENTS);
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
                System.err.println("PORT "+portstr+" MUST BE AN INT");
                System.exit(1);
            }
            AirlineRestClient client = new AirlineRestClient(hostName, port);
            try{
                if(prettyprint && !createFlight){
                    throw new Exception("-print cannot be specified without new flight creation");
                }
                if(search)
                {
                    if(searchSrc == null || searchDest == null) {
                       client.getAirlineInfo(airlineName);                    }
                    else {
                        client.getAirlineInfoSRCDEST(airlineName,searchSrc,searchDest);                    }
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
                       throw new Exception("Less arguments to create Flight");
                    }else{
                        throw new Exception("Extra arguments to create Flight");
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
                if(listiterator.hasNext()) {
                    portstr = listiterator.next();
                }else{
                    portstr=null;
                }
            }else if(param.equals("-host")){
                if(listiterator.hasNext()) {
                    hostName = listiterator.next();
                }else{
                    hostName=null;
                }
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