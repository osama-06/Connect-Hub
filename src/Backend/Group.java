package Backend;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id; // Unique identifier
    private String name;
    private String description;
    private String photoPath;
    private User primaryAdmin; // Primary admin as a User object
    private List<User> admins; // List of admin User objects
    private List<User> members; // List of member User objects
    private List<Post> posts; // List of posts in the group

    public Group(String id, String name, String description, String photoPath, User primaryAdmin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.primaryAdmin = primaryAdmin;
        this.admins = new ArrayList<>();
        this.members = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.admins.add(primaryAdmin); // Primary admin is added as an admin by default
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public User getPrimaryAdmin() {
        return primaryAdmin;
    }

    public void setPrimaryAdmin(User primaryAdmin) {
        this.primaryAdmin = primaryAdmin;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Post> getPosts() {
        return posts;
    }

    // Add an admin
    public void addAdmin(User admin) {
        if (!admins.contains(admin)) {
            admins.add(admin);
        }
    }

    // Remove an admin
    public void removeAdmin(User admin) {
        if (!admin.equals(primaryAdmin)) { // Primary admin cannot be removed
            admins.remove(admin);
        }
    }

    // Add a member
    public void addMember(User member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    // Remove a member
    public void removeMember(User member) {
        members.remove(member);
    }

    // Add a post
    public void addPost(Post post) {
        posts.add(post);
    }

    // Remove a post
    public void removePost(Post post) {
        posts.remove(post);
    }
}
