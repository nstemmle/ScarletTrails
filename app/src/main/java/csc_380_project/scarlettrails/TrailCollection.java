package csc_380_project.scarlettrails;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nathan on 10/17/2014.
 */
class TrailCollection {
    private ArrayList<Trail> trails;

    TrailCollection() {
        trails = new ArrayList<Trail>();
    }

    TrailCollection(int capacity) {
        //Default capacity is 10 but you can this constructor to assign a specific initial size
        trails = new ArrayList<Trail>(capacity);
    }

    int getSize() {
        return trails.size();
    }

    public void sortByIds() {
        Collections.sort(trails, Trail.getTrailIdComaparator());
    }

    public void sortByName() {
        Collections.sort(trails, Trail.getTrailNameComaparator());
    }

    public void sortByRating() {
        Collections.sort(trails, Trail.getTrailRatingComaparator());
    }

    public void sortByRatingDescending() {
        Collections.sort(trails, Trail.getTrailRatingDescendingComaparator());
    }

    public void sortByDistance() {
        Collections.sort(trails, Trail.getTrailDistanceComaparator());
    }

    public void sortByElevation() {
        Collections.sort(trails, Trail.getTrailElevationComaparator());
    }

    /*public void sortByDifficulty() {
        Collections.sort(trails, Trail.getTrailDifficultyComaparator());
    }*/

    /*public void sortByDuration() {
        Collections.sort(trails, Trail.getTrailDurationComaparator());
    }*/

    Trail getTrailById(int trailId) {
        sortByIds();
        Trail dummyTrail = new Trail(trailId, null, null, null, "", "", null, null, null, false);
        //Collections framework requires searching the list for an object so a dummy object
        //is created in this scope with the trailID initialized

        int index = Collections.binarySearch(trails, dummyTrail, Trail.getTrailIdComaparator());
        //index will be positive if the trail is found in the list
        //If the trail represented by the given id is not found in the list,
        // a negative value will be returned indicating where it would be inserted into the list

        if (index >= 0)
            return trails.get(index);

        //No trail was found matching the given Id
        return null;
    }

    //Returns a subcollection of this collection based on a minimum rating
    public TrailCollection createSubCollectionByMinimumRating(Double min) {
        TrailCollection subCollection = new TrailCollection();
        sortByRatingDescending();
        for (Trail t: trails) {
            if (t.getRating() < min)
                break;
            subCollection.trails.add(t);
        }
        return subCollection;
    }

}
