package ketank.bloodbank.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ketan on 9/24/2016.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        try {


            String recentToken = FirebaseInstanceId.getInstance().getToken();

            Log.d(REG_TOKEN, recentToken);
        }catch (Exception e){}
    }
}
