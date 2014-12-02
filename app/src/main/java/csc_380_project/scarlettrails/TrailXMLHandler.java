package csc_380_project.scarlettrails;

//@nstemmle
// 11/30/2014

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.ArrayList;

public class TrailXMLHandler extends DefaultHandler {
    private static final String TAG_TRAIL = "Trail";
    private static final String TAG_SEGMENT = "Segment";
    private static final String TAG_COORD = "Coordinate";

    //List of attributes for Trail tag
    private static final String[] ATT_TRAIL = {"id", "name", "segments", "length", "type", "park", "descriptor", "rating"};
    //Attribute for Segment Tag
    private static final String ATT_SEG_NUMCOORDS = "numCoords";
    //List of attributes for Coordinate tag
    private static final String[] ATT_COORD = {"id", "lat", "lng"};

    private String[] valuesTrail;

    //Keeping track of all trails
    private TrailCollection trails;

    //Used for current Trail
    private List<List<CustomLocation>> trailSegments;

    //Used for current TrailSegment
    private List<CustomLocation> coordinates;

    public TrailXMLHandler() {
        trails = new TrailCollection();
        valuesTrail = new String[ATT_TRAIL.length];
    }

    @Override
    public void startElement(String uri, String localName, String tagName, Attributes att) throws SAXException {
        switch (tagName) {
            case TAG_TRAIL: //Start of new Trail
                //Fetch Trail values
                for (int i = 0; i < ATT_TRAIL.length; i++)
                    valuesTrail[i] = att.getValue(ATT_TRAIL[i]);
                trailSegments = new ArrayList<List<CustomLocation>>(Integer.valueOf(valuesTrail[2]));
                break;
            case TAG_SEGMENT: //Start of new Segment
                coordinates = new ArrayList<CustomLocation>(Integer.valueOf(att.getValue(ATT_SEG_NUMCOORDS)));
                break;
            case TAG_COORD: //Add new coordinate
                coordinates.add(new CustomLocation(valuesTrail[0], att.getValue(ATT_COORD[0]), Double.valueOf(att.getValue(ATT_COORD[1])), Double.valueOf(att.getValue(ATT_COORD[2]))));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String tagName) throws SAXException {
        switch (tagName) {
            case TAG_TRAIL: //End of Trail
                //public Trail(String trailId, String name, int length, String type, String park, String descriptor, List<List<CustomLocation>> coordinateLists)
                trails.addTrail(new Trail(valuesTrail[0], valuesTrail[1], Integer.valueOf(valuesTrail[3]), valuesTrail[4], valuesTrail[5], valuesTrail[6], Double.valueOf(valuesTrail[7]), trailSegments, trailSegments.get(0).get(0)));
                break;
            case TAG_SEGMENT: //End of Segment
                trailSegments.add(coordinates);
                break;
        }
    }

    /*@Override
    public void endDocument() {
        trails.printSelf();
    }*/

    TrailCollection returnTrails() {
        return trails;
    }
}