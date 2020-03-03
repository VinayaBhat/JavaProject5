package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;



public class XMLDumper<Airline extends AbstractAirline> implements AirlineDumper<Airline>{
    Document document;
    StringWriter stringWriter;

    public XMLDumper(StringWriter sw){
        try{
            this.stringWriter=sw;
            AirlineXmlHelper helper = new AirlineXmlHelper();
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(helper);
            documentBuilder.setEntityResolver(helper);

            document = documentBuilder.newDocument();
        }catch (Exception e){
            System.err.println("Error while initializing XML dumper");
            System.exit(1);
        }

    }

    @Override
    public void dump(Airline airl) throws IOException {
        Airline airline = null;
        try {
            airline=airl;
            Element airlineobj = document.createElement("airline");
            document.appendChild(airlineobj);

            Element airlinename = document.createElement("name");
            airlinename.appendChild(document.createTextNode(airline.getName()));
            airlineobj.appendChild(airlinename);

            Collection<Flight> f=airline.getFlights();
            for(Flight fligthinfo:f) {
                Element flight = document.createElement("flight");
                airlineobj.appendChild(flight);

                Element flightnumber = document.createElement("number");
                flightnumber.appendChild(document.createTextNode(String.valueOf(fligthinfo.getNumber())));
                flight.appendChild(flightnumber);

                Element src = document.createElement("src");
                src.appendChild(document.createTextNode(fligthinfo.getSource()));
                flight.appendChild(src);

                Element depart = document.createElement("depart");
                flight.appendChild(depart);

                String[] ddatetime=getDateandTime(fligthinfo.getXmldeparture());

                Element date = document.createElement("date");
                Attr day = document.createAttribute("day");
                day.setValue(ddatetime[0]);
                date.setAttributeNode(day);
                Attr month = document.createAttribute("month");
                month.setValue(ddatetime[1]);
                date.setAttributeNode(month);
                Attr year = document.createAttribute("year");
                year.setValue(ddatetime[2]);
                date.setAttributeNode(year);
                depart.appendChild(date);

                Element time = document.createElement("time");
                Attr hour = document.createAttribute("hour");
                hour.setValue(ddatetime[3]);
                time.setAttributeNode(hour);
                Attr minute = document.createAttribute("minute");
                minute.setValue(ddatetime[4]);
                time.setAttributeNode(minute);
                depart.appendChild(time);


                Element dest = document.createElement("dest");
                dest.appendChild(document.createTextNode(fligthinfo.getDestination()));
                flight.appendChild(dest);

                Element arrive = document.createElement("arrive");
                flight.appendChild(arrive);
                String[] adatetime=getDateandTime(fligthinfo.getXmlarrival());

                Element date1 = document.createElement("date");
                Attr day1 = document.createAttribute("day");
                day1.setValue(adatetime[0]);
                date1.setAttributeNode(day1);
                Attr month1 = document.createAttribute("month");
                month1.setValue(adatetime[1]);
                date1.setAttributeNode(month1);
                Attr year1 = document.createAttribute("year");
                year1.setValue(adatetime[2]);
                date1.setAttributeNode(year1);
                arrive.appendChild(date1);

                Element time1 = document.createElement("time");
                Attr hour1 = document.createAttribute("hour");
                hour1.setValue(adatetime[3]);
                time1.setAttributeNode(hour1);
                Attr minute1 = document.createAttribute("minute");
                minute1.setValue(adatetime[4]);
                time1.setAttributeNode(minute1);
                arrive.appendChild(time1);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "us-ascii");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd");
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource,  new StreamResult(this.stringWriter));
        }catch (Exception e){
           System.err.println(e.getMessage());
           System.exit(1);
        }
    }

    public String[] getDateandTime(String datetime){
        String[] result=new String[5];
        try{
            Calendar calendar = Calendar.getInstance();
            DateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm");
            calendar.setTime(sdf.parse(datetime));
            result[0]=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            result[1]=String.valueOf(calendar.get(Calendar.MONTH)+1);
            result[2]=String.valueOf(calendar.get(Calendar.YEAR));
            result[3]=String.valueOf(calendar.get(Calendar.HOUR));
            result[4]=String.valueOf(calendar.get(Calendar.MINUTE));

        }catch (Exception e){
            System.err.println("Error while parsing date in XMLDumper "+e.getMessage());
            System.exit(1);
        }
        return result;
    }

    public String XMLDumperWriter(){
        return stringWriter.toString();
    }
}
