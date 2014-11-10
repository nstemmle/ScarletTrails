package csc_380_project.scarlettrails;

public class Comment {
    private final String commentId;
    private final String commentText; //Location of picture
    private final String profileOwnerId;
    private final String profileUsername;
    private final String trailOwnerId;
    private final String dateSubmitted; //Could be a String in a specific format such as “MMDDYYYY-HHMMSS” or a Calendar or similar object

    public Comment(String commentId, String commentText, String profileOwnerId, String profileUsername,
                   String trailOwnerId, String dateSubmitted) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.profileOwnerId = profileOwnerId;
        this.profileUsername = profileUsername;
        this.trailOwnerId = trailOwnerId;
        this.dateSubmitted = dateSubmitted;
    }

}
