package csc_380_project.scarlettrails;

import java.util.Calendar;

/**
 * Created by Nathan on 10/16/2014.
 */
public class Picture {
    private final String pictureId;
    private final String pictureContent; //Location of picture
    private final String profileOwnerId;
    private final String profileUsername;
    private final String trailOwnerId;
    private final String dateSubmitted; //Could be a String in a specific format such as “MMDDYYYY-HHMMSS” or a Calendar or similar object
    private Double rating;
    private CommentCollection comments;

    public Picture(String pictureId, String pictureContent, String profileOwnerId, String profileUsername,
                                                                String trailOwnerId, String dateSubmitted) {
        this.pictureId = pictureId;
        this.pictureContent = pictureContent;
        this.profileOwnerId = profileOwnerId;
        this.profileUsername = profileUsername;
        this.trailOwnerId = trailOwnerId;
        this.dateSubmitted = dateSubmitted;
    }

}
