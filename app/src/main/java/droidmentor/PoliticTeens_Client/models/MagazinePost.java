package droidmentor.PoliticTeens_Client.models;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MagazinePost {

    public String title;
    public String content;
    public String picture;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public MagazinePost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public MagazinePost(String title, String content, String picture) {
        this.title = title;
        this.content = content;
        this.picture = picture;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("picture", picture);
        result.put("starCount", starCount);
        result.put("stars", stars);
        return result;
    }
    // [END post_to_map]

}
// [END post_class]