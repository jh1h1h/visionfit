package Team10_VisionFit.Backend.firebaseAuthentication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.teamten.visionfit.R;


public class EditProfile extends AppCompatActivity{

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button save_changes_button = (Button) findViewById(R.id.save_changes_button);
        save_changes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log Log = null;
                Log.d("Button Check", "Squats Button Clicked");
                savedChanges();
            }
        });
    }
    public void savedChanges(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_profile);
        dialog.show();
        }
}

