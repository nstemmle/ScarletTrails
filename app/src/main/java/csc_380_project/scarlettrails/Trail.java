package csc_380_project.scarlettrails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Nathan on 10/16/2014.
 */
class Trail implements DatabaseInterface {
    public static final String DURATION_SHORT = "Short";
    public static final String DURATION_MEDIUM = "Medium";
    public static final String DURATION_LONG = "Long";
    public static final String DURATION_MARATHON = "Marathon";

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Normal";
    public static final String DIFFICULTY_CHALLENGING = "Challenging";
    public static final String DIFFICULTY_EXTREME = "Extreme";

    private final int trailId;
    private final String name;
    private final Double distance; //Distance in yards, feet, or meters? Should units be changeable?
    private final Double elevation;
    private final String duration;
    private final String difficulty;
    private final Location mLocation;
    private final String gear;
    private final String trailConditions;
    private final boolean petFriendly;
    private PictureCollection pictures;
    private PointOfInterestCollection POIs;
    private Double rating;
    private Forecast mForecast;

    Trail(int trailId, String name, Double distance, Double elevation, String duration, String difficulty,
                 Location mLocation, String gear, String trailConditions, boolean petFriendly) {
        this.trailId = trailId;
        this.name = name;
        this.distance = distance;
        this.elevation = elevation;
        this.duration = duration;
        this.difficulty = difficulty;
        this.mLocation = mLocation;
        this.gear = gear;
        this.trailConditions = trailConditions;
        this.petFriendly = petFriendly;

        //Random rating for now
        Random r = new Random();
        setRating(r.nextDouble() + 4);
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

    public int getTrailId() {
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

    public Location getLocation() {
        return mLocation;
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

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailIdComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.trailId - rhs.trailId;
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

    @Override
    public void query(String lookup) {

    }

    @Override
    public void updateData() {

    }

    @Override
    public String insertData() {
        return null;
    }
}
