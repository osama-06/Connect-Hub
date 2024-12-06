package Backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRepository {
    private static final String FILE_PATH = "posts.json";
    private List<Post> posts;

    public PostRepository() {
        initializeFile(); // Ensure the file exists
        this.posts = loadPosts(); // Load posts from the file
    }

    private List<Post> loadPosts() {
        File file = new File(FILE_PATH);

        // Check if the file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Return an empty list if the file is empty
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Post>>() {}.getType();
            List<Post> loadedPosts = new Gson().fromJson(reader, listType);

            // If the file contains invalid JSON, return an empty list
            return loadedPosts != null ? loadedPosts : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void savePosts() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(posts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPost(Post post) {
        posts.add(post);
        savePosts();
    }

    public List<Post> getPosts() {
        return posts;
    }

    // Fetch posts by a specific user
    public List<Post> getUserPosts(String userId) {
        // Filter the posts by the user ID
        return posts.stream()
                .filter(post -> post.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Method to initialize the file if it does not exist
    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file)) {
                writer.write("[]"); // Initialize with an empty JSON array
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
