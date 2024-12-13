package Backend;

public class Friend extends User{
    

    public Friend(String username, String profilePhoto) {
       super.setUsername(username);
        super.setProfilePhoto(profilePhoto);
        
    }

    
    @Override
    public String toString() {
        return "Friend{" +
                "username='" + super.getUsername() + '\'' +
                ", profilePhoto='" + super.getProfilePhoto() + '\'' +
                '}';
    }
}
