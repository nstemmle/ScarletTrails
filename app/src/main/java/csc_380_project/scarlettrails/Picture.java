package csc_380_project.scarlettrails;

import java.util.Calendar;

/**
 * Created by Nathan on 10/16/2014.
 */
public class Picture {
    private final int pictureId;
    private final String mURL; //Location of picture
    private final int locationId;
    private final int profileOwnerId;
    private final int trailOwnerId;
    private final String dateSubmitted; //Could be a String in a specific format such as “MMDDYYYY-HHMMSS” or a Calendar or similar object
    private Double rating;
    private CommentCollection comments;

    public Picture(int pictureId, String mURL, int locationId, int profileOwnerId, int trailOwnerId, String dateSubmitted) {
        this.pictureId = pictureId;
        this.mURL = mURL;
        this.locationId = locationId;
        this.profileOwnerId = profileOwnerId;
        this.trailOwnerId = trailOwnerId;
        this.dateSubmitted = dateSubmitted;
    }

}
