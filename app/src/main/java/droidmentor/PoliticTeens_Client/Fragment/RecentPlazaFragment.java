package droidmentor.PoliticTeens_Client.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import droidmentor.PoliticTeens_Client.S;

public class RecentPlazaFragment extends PlazaFragment {

    public RecentPlazaFragment() {}

    @Override
    public Query getQuery1(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("plaza_posts/")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
    @Override
    public Query getQuery2(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("plaza_posts/").orderByChild("starCount")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}