package droidmentor.PoliticTeens_Client.models;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class JungConPost {

    public String uid;
    public String author;
    public String title;
    public String body;
    public String category;
    public String color;
    public int starCount = 0;
    public int myClubID;
    public Map<String, Boolean> stars = new HashMap<>();

    public JungConPost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public JungConPost(String uid, String author, String title, String body, String category, String color) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.category = category;
        this.color = color;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("category", category);
        result.put("color",color);
        return result;
    }
    // [END post_to_map]

}
// [END post_class]