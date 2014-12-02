package csc_380_project.scarlettrails;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Nathan on 10/16/2014.
 */
class Trail implements Parcelable {
    public static final String DURATION_SHORT = "Short";
    public static final String DURATION_MEDIUM = "Medium";
    public static final String DURATION_LONG = "Long";
    public static final String DURATION_MARATHON = "Marathon";

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Normal";
    public static final String DIFFICULTY_CHALLENGING = "Challenging";
    public static final String DIFFICULTY_EXTREME = "Extreme";

    private final String trailId;
    private final String name;
    private final String type;
    private final String park;
    private final Double length; //Distance in yards, feet, or meters? Should units be changeable?
    private final String description;
    private final CustomLocation mCustomLocation;

    private PictureCollection pictures;
    private PointOfInterestCollection POIs;
    private Double rating;
    private Forecast mForecast;

    Trail(String trailId, String name, Double length, Double elevation, String park, String duration, String description, String type, String difficulty,
                CustomLocation mCustomLocation, String gear,  boolean petFriendly) {
        this.trailId = trailId;
        this.name = name;
        this.length = length;
        this.description = description;
        this.park = park;
        this.type = type;
        this.mCustomLocation = mCustomLocation;


        //Random rating for now
        Random r = new Random();
        setRating(r.nextDouble() + 4);
    }

    //Parcels must be retrieved FIFO - retrieve things first that were put in first
    /*
        dest.writeString(trailId);
        dest.writeString(name);
        dest.writeDouble(distance);
        dest.writeDouble(elevation);
        dest.writeString(duration);
        dest.writeString(difficulty);
        dest.writeString(gear);
        dest.writeString(trailConditions);
        dest.writeInt((petFriendly) ? 1 : 0);
        dest.writeDouble((rating != null) ? rating : -1.0);
        if (mCustomLocation != null)
            mCustomLocation.writeToParcel(dest, flags);
     */
    Trail(Parcel in) {
        trailId = in.readString();
        name = in.readString();
        length = in.readDouble();
        description = in.readString();
        type = in.readString();
        park = in.readString();
        Double tempRating = in.readDouble();
        rating = tempRating >= 0 ? tempRating : null;

        //Next values should be CustomLocation values if values exist
        if (in.dataAvail() > 0)
            mCustomLocation = new CustomLocation(in);
        else {
            mCustomLocation = null;
            Log.e("Trail.java","Custom Location in Trail(Parcel in) was null; debug");
        }
    }

    public Double getRating() {
        return rating;
    }

    public String getTrailId() {
        return trailId;
    }

    public String getName() {
        return name;
    }

    public Double getLength() {
        return length;
    }

    public String getDescription(){ return description; }

    public CustomLocation getLocation() {
        return mCustomLocation;
    }

    public void setRating(Double d) {
        this.rating = d;
    }

    public String getType(){ return type; }

    public String getPark() { return park; }



    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailIdComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.trailId.compareTo(rhs.trailId);
            }
        };
    }
    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailNameComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailRatingComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.rating.compareTo(rhs.rating);
            }
        };
    }

    static Comparator<Trail> getTrailRatingDescendingComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return rhs.rating.compareTo(lhs.rating);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailDistanceComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.length.compareTo(rhs.length);
            }
        };
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
        dest.writeDouble(length);
        dest.writeString(type);
        dest.writeString(park);

        dest.writeString(type);
        dest.writeDouble((rating != null) ? rating : -1.0);
        if (mCustomLocation != null)
            mCustomLocation.writeToParcel(dest, flags);
    }
}
