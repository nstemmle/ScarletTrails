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
    private final Double distance; //Distance in yards, feet, or meters? Should units be changeable?
    private final Double elevation;
    private final String duration;
    private final String difficulty;
    private final CustomLocation mCustomLocation;
    private final String gear;
    private final String trailConditions;
    private final boolean petFriendly;
    private PictureCollection pictures;
    private PointOfInterestCollection POIs;
    private Double rating;
    private Forecast mForecast;

    Trail(String trailId, String name, Double distance, Double elevation, String duration, String difficulty,
                CustomLocation mCustomLocation, String gear, String trailConditions, boolean petFriendly) {
        this.trailId = trailId;
        this.name = name;
        this.distance = distance;
        this.elevation = elevation;
        this.duration = duration;
        this.difficulty = difficulty;
        this.mCustomLocation = mCustomLocation;
        this.gear = gear;
        this.trailConditions = trailConditions;
        this.petFriendly = petFriendly;

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
        distance = in.readDouble();
        elevation = in.readDouble();
        duration = in.readString();
        difficulty = in.readString();
        gear = in.readString();
        trailConditions = in.readString();
        petFriendly = in.readInt() == 1;
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

    public Forecast getForecast() {
        return mForecast;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public String getTrailId() {
        return trailId;
    }

    public String getName() {
        return name;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getElevation() {
        return elevation;
    }

    public String getDuration() {
        return duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public CustomLocation getLocation() {
        return mCustomLocation;
    }

    public String getGear() {
        return gear;
    }

    public String getTrailConditions() {
        return trailConditions;
    }

    public void setRating(Double d) {
        this.rating = d;
    }
    
    public Forecast createForecast(){
        if (mForecast == null) {
            String info = ((new ForecastWrapper().getWeatherContent(mCustomLocation.getLatitude(), mCustomLocation.getLongitude(), true)));
            try {
                mForecast = ForecastWrapper.getWeather(info, 0);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mForecast;
    }
    
    

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
                return lhs.distance.compareTo(rhs.distance);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailElevationComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.elevation.compareTo(rhs.elevation);
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    /*static Comparator<Trail> getTrailDifficultyComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.difficulty - rhs.difficulty;
            }
        };
    }*/

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    /*static Comparator<Trail> getTrailDurationComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.duration - rhs.duration;
            }
        };
    }*/

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
    }
}
