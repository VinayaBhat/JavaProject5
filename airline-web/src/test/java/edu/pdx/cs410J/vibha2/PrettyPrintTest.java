package edu.pdx.cs410J.vibha2;

import edu.pdx.cs410J.ParserException;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrettyPrintTest {
    @Test
    public void PrettyPrinttest() throws ParseException {
        Flight flight = new Flight("5");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/20/2020", "11:30 am");
        flight.setArrival_time("1/21/2020", "10:30 am");
        Airline airline=new Airline("Alaska");
        airline.addFlight(flight);
        PrettyPrint pp=new PrettyPrint();
        pp.sortFlightsandPrint(airline);
    }

    Airline<Flight> a1;
    /**
     * Testing the dump functions which writes to xml file
     */
    @Test
    public void testingxmldump() throws IOException {

        StringWriter stringWriter = new StringWriter();
        stringWriter.flush();
        a1=new Airline<>("Alaska");
        Flight flight = new Flight("5");
        flight.setSource("JFK");
        flight.setDestination("PDX");
        flight.setDeparture_time("1/20/2020", "11:40 pm");
        flight.setArrival_time("1/21/2020","10:30 pm");
        a1.addFlight(flight);
        XMLDumper td=new XMLDumper(stringWriter);
        td.dump(a1);
    }

    @Test
    public void XMLDumperTest() throws ParserConfigurationException, IOException, SAXException, ParserException {
        String content="<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>\n" +
                "<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
                "<airline>\n" +
                "    <name>Project4</name>\n" +
                "    <flight>\n" +
                "        <number>888</number>\n" +
                "        <src>CLE</src>\n" +
                "        <depart>\n" +
                "            <date day=\"7\" month=\"1\" year=\"17\"/>\n" +
                "            <time hour=\"7\" minute=\"0\"/>\n" +
                "        </depart>\n" +
                "        <dest>EGE</dest>\n" +
                "        <arrive>\n" +
                "            <date day=\"17\" month=\"1\" year=\"17\"/>\n" +
                "            <time hour=\"19\" minute=\"0\"/>\n" +
                "        </arrive>\n" +
                "    </flight>\n" +
                "</airline>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(content));

        XMLParser parser = new XMLParser(builder.parse(is));
        Airline<Flight> airline=new Airline("Project4");
        Flight flight1=new Flight("888");
        flight1.setSource("CLE");
        flight1.setDestination("EGE");
        flight1.setDeparture_time("01/07/2017","7:00 am");
        flight1.setArrival_time("01/17/2017","7:00 pm");
        airline.addFlight(flight1);
        assertThat(airline.toString(),containsString(parser.parse().toString()));
    }

}
