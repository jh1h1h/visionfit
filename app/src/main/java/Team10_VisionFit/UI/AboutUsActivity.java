package Team10_VisionFit.UI;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.teamten.visionfit.R;

public class AboutUsActivity extends BaseActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_settings);
    }

    //@Override
    //public void onBackPressed() {
        // Do nothing (disable back button functionality)
    //}
}
