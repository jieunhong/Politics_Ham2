package droidmentor.PoliticTeens_Client;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by User on 2017-09-25.
 */

public class GetUserId {
    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
