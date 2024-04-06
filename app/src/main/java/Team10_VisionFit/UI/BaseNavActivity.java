package Team10_VisionFit.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.teamten.visionfit.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;

public class BaseNavActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null, false));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
//        if (view instanceof RelativeLayout) {
//            ViewGroup parent = (ViewGroup) findViewById(R.id.main_container);
        ConstraintLayout constraintLayout = (ConstraintLayout) view;
        View bottomNav = LayoutInflater.from(this).inflate(R.layout.activity_bottom_nav_bar, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.setMargins(16, 30, 16, 20);
//            layoutParams.bottomMargin(20);
//            layoutParams.leftMargin(16);
//            layoutParams.rightMargin(16);
//            layoutParams.topMargin(30);

        bottomNav.setLayoutParams(layoutParams);
        constraintLayout.addView(bottomNav, (constraintLayout.getChildCount()-1) + 1);

    }
//}

    public void setUpBottomNavBar(int selectedItemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Log.d("Button Check", "Home Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    Log.d("Button Check", "Settings Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                //case R.id.cameraButton:
                    //Intent intent = new Intent(getApplicationContext(), LivePreviewActivity.class);
                    //intent.putExtra("ClassType", "Free Style");
                    //startActivity(intent);
                    //Log.d("Button Check", "Camera Button Clicked");
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    //finish();
                    //return true;

                case R.id.bottom_userProfile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    Log.d("Button Check", "Profile Button Clicked");
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_logout:
                    Log.d("Button Check", "Logout Button Clicked");
                    customExitDialog();
                    return true;
            }
            return false;
        });
    }

    public  void customExitDialog()
    {
        // creating custom dialog
        final Dialog dialog = new Dialog(this);

        // setting content view to dialog
        dialog.setContentView(R.layout.logout_dialog_box);

        // getting reference of TextView
        TextView dialogButtonYes = (TextView) dialog.findViewById(R.id.textViewYes);
        TextView dialogButtonNo = (TextView) dialog.findViewById(R.id.textViewNo);

        // click listener for No
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog
                dialog.dismiss();

            }
        });
        // click listener for Yes
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog and exit the exit
                dialog.dismiss();

                Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                SharedPreferences sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                finish();

            }
        });

        // show the exit dialog
        dialog.show();
    }
}