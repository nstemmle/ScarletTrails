package csc_380_project.scarlettrails;

public class Profile  {
    private  String profileId;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String dateOfBirth;
    private  String username;
    private  String password;

    public Profile(String profileId, String firstName,
                   String lastName, String email, String dateOfBirth, String username) {
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
    }

    public String getProfileId() { return profileId; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getUsername() { return username; }

    public String getDateOfBirth() { return dateOfBirth; }

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
}
