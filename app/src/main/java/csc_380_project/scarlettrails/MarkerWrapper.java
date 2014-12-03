package csc_380_project.scarlettrails;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by nstemmle on 12/3/14.
 */
public class MarkerWrapper {
    private final double latitude;
    private final double longitude;
    private String id;
    private final String trailId;
    private final String title;

    MarkerWrapper(double latitude, double longitude, String id, String associatedId, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.trailId = associatedId;
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getTrailId() {
        return trailId;
    }

    public String getTitle() {
        return title;
    }

    public Marker createMarker(GoogleMap mMap, LocationWrapper locationWrapper) {
        Marker result = locationWrapper.addMarkerAtLocation(mMap, latitude, longitude, title, false);
        id = result.getId();
        return result;
    }
}