package csc_380_project.scarlettrails;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nstemmle on 12/3/14.
 */
public class MarkerCollection {
    private List<MarkerWrapper> markers;

    MarkerCollection() {
        markers = new ArrayList<MarkerWrapper>();
    }

    MarkerCollection(int capacity) {
        markers = new ArrayList<MarkerWrapper>(capacity);
    }

    public void clear() {
        markers.clear();
    }

    public int getSize() {
        return markers.size();
    }

    public MarkerWrapper getMarkerAtIndex(int index) {
        return markers.get(index);
    }

    public void addMarker(MarkerWrapper m) {
        markers.add(m);
    }

    //Probably need to ask for the last three parameters
    public void addMarker(Marker m) {
        markers.add(new MarkerWrapper(m.getPosition().latitude, m.getPosition().longitude, m.getId(), "", ""));
    }

    public void removeMarker(String id) {
        markers.remove(getMarkerById(id));
    }

    public void sortById() {
        Collections.sort(markers, getMarkerIdComparator());
    }

    public void sortByTitle() {
        Collections.sort(markers, getMarkerTitleComparator());
    }

    public void sortByTrailId() {
        Collections.sort(markers, getMarkerTrailIdComparator());
    }

    MarkerWrapper getMarkerById(String id) {
        sortById();
        MarkerWrapper dummymarker = new MarkerWrapper(0,0,id,"","");

        int index = Collections.binarySearch(markers, dummymarker, getMarkerIdComparator());

        if (index >= 0)
                return markers.get(index);

        return null;
    }


    MarkerWrapper getMarkerByTitle(String title) {
        sortByTitle();
        MarkerWrapper dummymarker = new MarkerWrapper(0,0,"","",title);

        int index = Collections.binarySearch(markers, dummymarker, getMarkerTitleComparator());

        if (index >= 0)
            return markers.get(index);

        return null;
    }

    MarkerWrapper getMarkerByTraidId(String trailId) {
        sortByTrailId();
        MarkerWrapper dummymarker = new MarkerWrapper(0,0,"", trailId,"");

        int index = Collections.binarySearch(markers, dummymarker, getMarkerTrailIdComparator());

        if (index >= 0)
            return markers.get(index);

        return null;
    }

    static Comparator<MarkerWrapper> getMarkerIdComparator() {
        return new Comparator<MarkerWrapper>() {
            @Override
            public int compare(MarkerWrapper lhs, MarkerWrapper rhs) {
                return lhs.getId().compareTo(rhs.getId());
            }
        };
    }

    static Comparator<MarkerWrapper> getMarkerTitleComparator() {
        return new Comparator<MarkerWrapper>() {
            @Override
            public int compare(MarkerWrapper lhs, MarkerWrapper rhs) {
                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        };
    }

    static Comparator<MarkerWrapper> getMarkerTrailIdComparator() {
        return new Comparator<MarkerWrapper>() {
            @Override
            public int compare(MarkerWrapper lhs, MarkerWrapper rhs) {
                return lhs.getTrailId().compareToIgnoreCase(rhs.getTrailId());
            }
        };
    }
}
