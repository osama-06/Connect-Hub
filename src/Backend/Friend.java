package Backend;

public class Friend {
    private String username;
    private String profilePhoto; // Friend's profile photo path

    public Friend(String username, String profilePhoto) {
        this.username = username;
        this.profilePhoto = profilePhoto;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "username='" + username + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
