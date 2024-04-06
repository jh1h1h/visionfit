package Team10_VisionFit.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.teamten.visionfit.R;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;

public class PrivacyPolicyActivity extends BaseNavActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        //To setup nav bar
        setUpBottomNavBar(R.id.bottom_settings);

    }

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}
