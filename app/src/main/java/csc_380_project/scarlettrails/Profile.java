package csc_380_project.scarlettrails;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private  String profileId;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String dateOfBirth;
    private  String username;
    private  String password;
    private  String interests;
    private  String pictureURL;

    public Profile(String profileId, String firstName,
                   String lastName, String email, String dateOfBirth, String username,
                                                  String interests, String pictureURL) {
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.interests = interests;
        this.pictureURL = pictureURL;
    }

    public String getProfileId() { return profileId; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getUsername() { return username; }

    public String getDateOfBirth() { return dateOfBirth; }

    public String getInterests() { return interests; }

    public String getPictureURL() { return pictureURL; }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    Profile(Parcel in) {
        profileId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        dateOfBirth = in.readString();
        username = in.readString();
        interests = in.readString();
        pictureURL = in.readString();
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel parcel) {
            return new Profile(parcel);
        }
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(dateOfBirth);
        dest.writeString(username);
        dest.writeString(interests);
        dest.writeString(pictureURL);
    }
}
