package pl.mczerwi.flarespredict.altitude;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marcin on 2015-04-11.
 */
public class GoogleAltitudeProvider implements AltitudeProvider {

    @Override
    public double getAltitude(double latitude, double longitude) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            String url = "http://maps.googleapis.com/maps/api/elevation/"
                    + "xml?locations=" + String.valueOf(latitude)
                    + "," + String.valueOf(longitude)
                    + "&sensor=true";
            Document doc = db.parse(new URL(url).openStream());
            NodeList nodes = doc.getElementsByTagName("elevation");
            if(nodes.getLength() != 1) {
                throw new IllegalStateException("There should be exactly one elevation node in the response.");
            }
            return Double.parseDouble(nodes.item(0).getTextContent());
        } catch(Throwable e) {
            throw new IllegalStateException("Error while getting altitude information from google server.", e);
        }
    }
}
