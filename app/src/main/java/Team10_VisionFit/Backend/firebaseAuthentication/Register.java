package Team10_VisionFit.Backend.firebaseAuthentication;

import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamten.visionfit.R;


public class Register extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupUsername, signupDOB, signupCountry;
    private Button signupButton;
    private TextView loginRedirectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.email);
        signupPassword = findViewById(R.id.password);
        signupUsername = findViewById(R.id.username_register);
        signupDOB = findViewById(R.id.DOB_register);
        signupCountry = findViewById(R.id.Country_register);
        signupButton = findViewById(R.id.btn_register);
        loginRedirectText = findViewById(R.id.loginNow);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String dob = signupDOB.getText().toString().trim();
                String username = signupUsername.getText().toString();
                String country = signupCountry.getText().toString();

                // Validate email
                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    return;
                }

                // Validate password
                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    return;
                }

                // Validate date of birth
                if (!isValidDateFormat(dob)) {
                    signupDOB.setError("Invalid date format. Use DD/MM/YYYY");
                    return;
                }

                // Validate country of residence (you can implement your own validation logic)
                if (!isValidCountry(country)) {
                    signupCountry.setError("Invalid country");
                    return;
                }

                // Proceed with user registration
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            FirebaseUser user=auth.getCurrentUser();
                            String uid=user.getUid();
                            // No need to validate dob and country again here
                            TestFirestoreActivity.addDataToFirestore(uid, username, country, dob, 0,0,0,0,0,0,0,0,0);
                            startActivity(new Intent(Register.this, Login.class));
                        } else {
                            Toast.makeText(Register.this, "Failed to Register" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        signupCountry.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Trigger the signupButton click listener action
                    signupButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean isValidDateFormat(String dob) {
        // Regular expression to match DD/MM/YYYY format
        String regex = "\\d{2}/\\d{2}/\\d{4}";
        return dob.matches(regex);
    }

    private boolean isValidCountry(String country) {
        // Convert the entered country to uppercase for case-insensitive comparison
        country = country.trim().toUpperCase();

        // Iterate through the available locales to check if the entered country matches any
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry().toUpperCase();

            // Check if the entered country matches the name of the current locale
            if (countryName.equals(country)) {
                return true; // Valid country
            }
        }

        return false; // Invalid country
    }
}