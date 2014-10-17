package csc_380_project.scarlettrails;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Nathan on 10/16/2014.
 */
public class Trail implements DatabaseInterface {
    private final int trailId;
    private final String name;
    private final Double distance; //Distance in yards, feet, or meters? Should units be changeable?
    private final Double elevation;
    private final int duration; //Estimated time in minutes to complete trail
    private final int difficulty; //Enumeration such as 1 = easy, 2 = normal, 3 = challenging, 4 = extreme or something like that
    private final Location mLocation;
    private final String gear;
    private final String trailConditions;
    private final boolean petFriendly;
    private PictureCollection pictures;
    private PointOfInterestCollection POIs;
    private Double rating;
    private Forecast mForecast;

    public Trail(int trailId, String name, Double distance, Double elevation, int duration, int difficulty,
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

    public int getDuration() {
        return duration;
    }

    public int getDifficulty() {
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
    static Comparator<Trail> getTrailDifficultyComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.difficulty - rhs.difficulty;
            }
        };
    }

    //Creates a comparator object that can be used to sort a collection of trails (TrailCollection) by the specified criteria
    //e.g. Collections.Sort(TrailCollectionInstance, Trail.getTrailIdComparator());
    static Comparator<Trail> getTrailDurationComaparator() {
        return new Comparator<Trail>() {
            @Override
            public int compare(Trail lhs, Trail rhs) {
                return lhs.duration - rhs.duration;
            }
        };
    }

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
