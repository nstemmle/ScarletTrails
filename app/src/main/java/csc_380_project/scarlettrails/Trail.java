package csc_380_project.scarlettrails;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;

import java.util.Comparator;
import java.util.ArrayList;
/**
 * Created by Nathan on 10/16/2014.
 */
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Nathan on 10/16/2014.
 */
public class Trail implements Parcelable {
    private final String trailId;
    private final String name;
    private final int length; //Distance in feet

    private final String type;
    private final String park;
    private final String descriptor;

    private final CustomLocation start;

    private List<TrailSegment> segments;

    private Double rating;

    public Trail(String trailId, String name, int length, String type, String park, String descriptor, double rating, List<List<CustomLocation>> coordinateLists, CustomLocation start) {
        this.trailId = trailId;
        this.name = name;
        this.length = length;
        this.type = type;
        this.park = park;
        this.descriptor = descriptor;
        this.rating = rating;
        this.start = start;

        if (coordinateLists == null)
            segments = new ArrayList<TrailSegment>();
        else
            segments = new ArrayList<TrailSegment>(coordinateLists.size());
        if (coordinateLists != null) {
            for (List<CustomLocation> coords : coordinateLists) {
                segments.add(new TrailSegment(coords));
            }
        }

        //start = segments.get(0).getLocation(0);
    }

    Trail(Parcel in) {
        trailId = in.readString();
        name = in.readString();
        length = in.readInt();
        type = in.readString();
        park = in.readString();
        descriptor = in.readString();
        rating = in.readDouble();
        start = new CustomLocation(in);

        segments = new ArrayList<TrailSegment>(in.readInt());

        while (in.dataAvail() > 0) {
            segments.add(new TrailSegment(in));
        }
    }

    //@nstemmle
    @SuppressLint("ParcelCreator")
    private class TrailSegment implements Parcelable {
        private List<CustomLocation> coordinates;

        public TrailSegment(List<CustomLocation> coordinates) {
            this.coordinates = coordinates;
        }

        /*public void printSelf() {
            for (CustomLocation loc : coordinates) {
                System.out.println(loc.toString());
            }
        }*/

        public CustomLocation getLocation(int index) {
            return coordinates.get(index);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        TrailSegment(Parcel in) {
            coordinates = new ArrayList<CustomLocation>(in.readInt());

            while (in.dataAvail() > 0) {
                coordinates.add(new CustomLocation(in));
            }
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(coordinates.size());

            for (CustomLocation loc : coordinates)
                loc.writeToParcel(dest, flags);
        }

        private void addTrailMarkersToMap(GoogleMap map, LocationWrapper locWrapper) {
            for (CustomLocation loc : coordinates) {
                locWrapper.addMarkerAtLocation(map, loc, "", false);
            }
        }

        /*public drawOnMap(GoogleMap map) {

        }*/
    }

    public String getTrailId() { return trailId; }

    public String getName() { return name; }

    public int getLength() { return length; }

    public String getType() { return type; }

    public String getPark() { return park; }

    public Double getRating() { return rating; }

    public String getDescriptor() { return descriptor; }

    public CustomLocation getLocation() {
        return start;
    }

    public void addTrailMarkersToMap(GoogleMap map, LocationWrapper locWrapper) {
        locWrapper.addMarkerAtLocation(map, start, name, true);
        locWrapper.centerCameraOnLocation(map, start, locWrapper.COUNTY_ZOOM);
        for (TrailSegment seg : segments) {
            seg.addTrailMarkersToMap(map, locWrapper);
        }
    }

    @Override
    public String toString() {
        return "trailId: " + trailId + ", name: " + name + ", segments: " + segments.size() + ", length: " + length + ", type: " + type + ", park: " + park + ", descriptor: " + descriptor + ", rating: " + rating;
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Trail> CREATOR = new Parcelable.Creator<Trail>() {
        public Trail createFromParcel(Parcel parcel) {
            return new Trail(parcel);
        }
        public Trail[] newArray(int size) {
            return new Trail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailId);
        dest.writeString(name);
        dest.writeInt(length);
        dest.writeString(type);
        dest.writeString(park);
        dest.writeString(descriptor);
        dest.writeDouble(rating);
        start.writeToParcel(dest, flags);
        //Number of segments
        dest.writeInt(segments.size());
        start.writeToParcel(dest,flags);
        for (TrailSegment segment : segments)
            segment.writeToParcel(dest, flags);
    }

    /*public void printSelf() {
        System.out.println(toString());
        for (TrailSegment segment : segments) {
            segment.printSelf();
        }
    }*/

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailIdComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                //return lhs.trailId.compareTo(rhs.trailId);
                return Integer.valueOf(lhs.trailId) - Integer.valueOf(rhs.trailId);
            }
        };
    }
    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailNameComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailParkComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.park.compareToIgnoreCase(rhs.park);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailRatingComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.rating.compareTo(rhs.rating);
            }
        };
    }

    static Comparator<Trail> getTrailRatingDescendingComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return rhs.rating.compareTo(lhs.rating);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailLengthComparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.length - rhs.length;
            }
        };
    }
}
