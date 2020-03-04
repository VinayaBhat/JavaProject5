package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLParser implements AirlineParser {
    Document document;

    public XMLParser(Document doc) {
        try {
            this.document=doc;
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder builder =
                    factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);


        } catch (Exception e) {
            System.err.println("Error while initializing XML parser " + e.getMessage());
            System.exit(1);
        }

    }

    @Override
    public Airline<Flight> parse() throws ParserException {
        Airline<Flight> airline = null;
        SimpleDateFormat inf = new SimpleDateFormat("MM/dd/yy HH:mm");
        SimpleDateFormat outf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        try {
            NodeList nodeList = document.getElementsByTagName("name");
            String airlinename = nodeList.item(0).getTextContent();

            if (!Airline.airlinenameisvalid(airlinename))
                throw new Exception("Airline name is not valid in XML file ");
            airline = new Airline<>(airlinename);
            NodeList flightList = document.getElementsByTagName("flight");
            for (int i = 0; i < flightList.getLength(); i++) {
                Node node = flightList.item(i);
                Element elem = (Element) node;
                String flightnum = elem.getElementsByTagName("number").item(0).getTextContent();
                if (!Flight.flightnumberisvalid(flightnum)) {
                    throw new Exception("Flight number is not valid in XML file ");
                }
                Flight flight = new Flight(flightnum);
                String src = elem.getElementsByTagName("src").item(0).getTextContent();
                if (!Flight.flightsrcdestisvalid(src)) {
                    throw new Exception("Flight src is not valid in XML file ");
                }
                flight.setSource(src);
                NodeList depart = elem.getElementsByTagName("depart");
                Node d = depart.item(0);
                Element da = (Element) d;
                NamedNodeMap dat = da.getElementsByTagName("date").item(0).getAttributes();
                String date = dat.getNamedItem("month").getTextContent() + "/" + dat.getNamedItem("day").getTextContent() + "/" + dat.getNamedItem("year").getTextContent();
                Node t = depart.item(0);
                Element ti = (Element) t;
                NamedNodeMap tim = ti.getElementsByTagName("time").item(0).getAttributes();
                String deptHour = tim.getNamedItem("hour").getTextContent();
                String deptMinute = tim.getNamedItem("minute").getTextContent();
                String deptfinal = date + " " + deptHour + ":" + deptMinute;
                Date deptdate = inf.parse(deptfinal);
                String deptstr = outf.format(deptdate);
                String[] departure = deptstr.split(" ");
                checkdatetime(departure[0], departure[1], departure[2]);
                flight.setDeparture_time(departure[0],departure[1]+" "+departure[2]);

                String dest = elem.getElementsByTagName("dest").item(0).getTextContent();
                if (!Flight.flightsrcdestisvalid(dest)) {
                    throw new Exception("Flight dest is not valid in XML file ");
                }
                flight.setDestination(dest);
                NodeList arrival = elem.getElementsByTagName("arrive");
                Node d1 = arrival.item(0);
                Element da1 = (Element) d1;
                NamedNodeMap dat1 = da1.getElementsByTagName("date").item(0).getAttributes();
                String date1 = dat1.getNamedItem("month").getTextContent() + "/" + dat1.getNamedItem("day").getTextContent() + "/" + dat1.getNamedItem("year").getTextContent();
                Node t1 = arrival.item(0);
                Element ti1 = (Element) t1;
                NamedNodeMap tim1 = ti1.getElementsByTagName("time").item(0).getAttributes();
                String arrivalHour = tim1.getNamedItem("hour").getTextContent();
                String arrivalMinute = tim1.getNamedItem("minute").getTextContent();
                String arrivalfinal = date1 + " " + arrivalHour + ":" + arrivalMinute;
                Date arrivaldate = inf.parse(arrivalfinal);
                String arrstr = outf.format(arrivaldate);
                String[] arrivals = arrstr.split(" ");
                checkdatetime(arrivals[0], arrivals[1], arrivals[2]);
                flight.setArrival_time(arrivals[0],arrivals[1]+" "+arrivals[2]);
                checkArrivalandDepTime(deptstr, arrstr);

                airline.addFlight(flight);
            }
        } catch (Exception e) {
            System.err.println("Error while reading XML file " + e.getMessage());
            System.exit(1);
        }

        return airline;
    }

    boolean checkArrivalandDepTime(String depart,String arrive){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            Date depdate = sdf.parse(depart);
            Date arrdate=sdf.parse(arrive);
            int check=depdate.compareTo(arrdate);
            if(check>0){
                throw new Exception("Arrival date before departure date in XML file ");
            }
        }catch (Exception e){
            System.err.println("Error while checking arrival date >= departure date in XML parser "+e.getMessage());
            System.exit(1);
        }
        return true;
    }

    public void checkdatetime(String date, String time, String ampm) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String finaldatetime = date + " " + time + " " + ampm;
        try{
            Date d = formatter.parse(finaldatetime);
        }
        catch (Exception e){
            System.err.println("Please verify the format for datetime in the XML file");
            System.exit(1);
        }
    }
}
