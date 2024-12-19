package Backend;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private String groupName;  // Using groupName as the identifier
    private String description;
    private String groupPhoto;
    private String primaryAdmin;
    private List<String> members; // List of member IDs
    private List<String> admins;  // List of admin IDs
    private List<Gpost> posts;    // List of posts in the group
        private List<String> pendingMembershipRequests;  // List of pending requests



    public Group() {
        // Initialize lists to avoid null pointer exceptions
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.posts = new ArrayList<>();
    }
    

    public Group(String groupName, String description, String groupPhoto, String primaryAdmin) {
        this.groupName = groupName;
        this.description = description;
        this.groupPhoto = groupPhoto;
        this.primaryAdmin = primaryAdmin;
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.posts = new ArrayList<>();

        // The primary admin is also a member and an admin by default
        this.members.add(primaryAdmin);
        this.admins.add(primaryAdmin);
    }
    
    
    
        public List<String> getPendingMembershipRequests() {
        return pendingMembershipRequests;
    }

    public void setPendingMembershipRequests(List<String> pendingMembershipRequests) {
        this.pendingMembershipRequests = pendingMembershipRequests;
    }

        public void addPendingMembershipRequest(String username) {
        if (!pendingMembershipRequests.contains(username)) {
            pendingMembershipRequests.add(username);
        }
    }

    public void removePendingMembershipRequest(String username) {
        pendingMembershipRequests.remove(username);
    }
    

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public String getPrimaryAdmin() {
        return primaryAdmin;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public List<Gpost> getPosts() {
        return posts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public void setPrimaryAdmin(String primaryAdmin) {
        this.primaryAdmin = primaryAdmin;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public void setPosts(List<Gpost> posts) {
        this.posts = posts;
    }

    public void removeMember(String userId) {
        members.remove(userId);  // Remove the user from the members list
    }

    // Add a new member to the group
    public boolean addMember(String userId) {
        if (!members.contains(userId)) {
            members.add(userId);
            return true;  // Successfully added member
        }
        return false;  // User is already a member
    }

    public void promoteToAdmin(String memberId) {
        if (!admins.contains(memberId)) {
            admins.add(memberId);
        }
    }

    public void demoteToMember(String adminId) {
        if (!adminId.equals(primaryAdmin)) {
            admins.remove(adminId);
        }
    }

    public boolean isAdmin(String userId) {
        return admins.contains(userId);
    }

    public boolean isPrimaryAdmin(String userId) {
        return primaryAdmin.equals(userId);
    }

    public void addPost(String content, String authorId) {
        Gpost post = new Gpost(content, authorId);
        posts.add(post);
    }

// Edit a post in the group by its content
public boolean editPost(String oldContent, String newContent) {
    // Iterate over posts to find the one with the matching old content
    for (Gpost post : posts) {
        if (post.getContent().equals(oldContent)) {
            post.setContent(newContent);  // Update the post's content
            return true;  // Return true when post content is updated
        }
    }
    return false;  // Return false if no post with the matching old content is found
}


// Delete a post from the group by its content
public boolean deletePost(String content) {
    // Iterate over posts to find the one with the matching content
    for (Gpost post : posts) {
        if (post.getContent().equals(content)) {
            posts.remove(post);  // Remove the post
            return true;  // Return true when post is deleted
        }
    }
    return false;  // Return false if no post with the matching content is found
}

}