package csc_380_project.scarlettrails;

import java.util.Calendar;

/**
 * Created by Nathan on 10/16/2014.
 */
public class Picture {
    private final String pictureId;
    private final String pictureUrl; //Location of picture
    private final String profileOwnerId;
    private final String profileUsername;
    private final String trailOwnerId;

    private final String trailName;
    private final String dateSubmitted; //Could be a String in a specific format such as “MMDDYYYY-HHMMSS” or a Calendar or similar object
    private Double rating;
    private CommentCollection comments;

    public Picture(String pictureId, String pictureUrl, String profileOwnerId, String profileUsername,
                                      String trailOwnerId, String trailName, String dateSubmitted) {
        this.pictureId = pictureId;
        this.pictureUrl = pictureUrl;
        this.profileOwnerId = profileOwnerId;
        this.profileUsername = profileUsername;
        this.trailOwnerId = trailOwnerId;
        this.trailName = trailName;
        this.dateSubmitted = dateSubmitted;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureId() {
        return pictureId;
    }

    public String getProfileOwnerId() {
        return profileOwnerId;
    }

    public String getProfileUsername() {
        return profileUsername;
    }

    public String getTrailOwnerId() {
        return trailOwnerId;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public Double getRating() {
        return rating;
    }

    public CommentCollection getComments() {
        return comments;
    }

    public String getTrailName() {
        return trailName;
    }
}
