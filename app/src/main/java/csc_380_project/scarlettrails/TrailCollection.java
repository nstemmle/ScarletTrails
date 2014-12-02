package csc_380_project.scarlettrails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nathan on 10/17/2014.
 */
public class TrailCollection implements Parcelable {
    private List<Trail> trails;

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

    public Trail getTrailAtIndex(int index) {
        return trails.get(index);
    }

    public void addTrail(Trail t) {
        trails.add(t);
    }

    public void removeTrail(String id) {
        trails.remove(getTrailById(id));
    }

    public void sortById() {
        Collections.sort(trails, Trail.getTrailIdComparator());
    }

    public void sortByName() {
        Collections.sort(trails, Trail.getTrailNameComparator());
    }

    public void sortByRating() {
        Collections.sort(trails, Trail.getTrailRatingComparator());
    }

    public void sortByRatingDescending() {
        Collections.sort(trails, Trail.getTrailRatingDescendingComparator());
    }

    public void sortByLength() {
        Collections.sort(trails, Trail.getTrailLengthComparator());
    }

    public void sortByPark() {
        Collections.sort(trails, Trail.getTrailParkComparator());
    }


    Trail getTrailbyName(String name) {
        sortByName();
        Trail dummytrail = new Trail(null,name,0,null,null,null,0d,null, null);

        int index = Collections.binarySearch(trails, dummytrail, Trail.getTrailNameComparator());

        if (index >= 0)
            return trails.get(index);

        return null;
    }

    Trail getTrailById(String trailId) {
        sortById();
        Trail dummyTrail = new Trail(trailId,null,0,null,null,null,0d,null, null);
        //Collections framework requires searching the list for an object so a dummy object
        //is created in this scope with the trailID initialized

        int index = Collections.binarySearch(trails, dummyTrail, Trail.getTrailIdComparator());
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

    public TrailCollection createSubCollectionByPark(String park) {
        TrailCollection subCollection = new TrailCollection();
        sortByPark();
        for (Trail t : trails) {
            if (t.getPark().compareToIgnoreCase(park) > 0)
                break;
            subCollection.trails.add(t);
        }
        return subCollection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(trails.size());

        for (Trail t : trails) {
            t.writeToParcel(dest,flags);
        }
    }

    TrailCollection(Parcel in) {
        trails = new ArrayList<Trail>(in.readInt());

        while (in.dataAvail() > 0) {
            trails.add(new Trail(in));
        }
    }

    public void addTrailMarkersToMap(GoogleMap map, LocationWrapper locWrapper) {
        for (Trail t : trails) {
            t.addTrailMarkersToMap(map, locWrapper);
        }
    }

    /*public void printSelf() {
        sortById();
        for (Trail t : trails) {
            t.printSelf();
        }
    }*/
}
