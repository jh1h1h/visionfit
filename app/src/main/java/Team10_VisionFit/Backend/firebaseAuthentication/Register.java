package Team10_VisionFit.Backend.firebaseAuthentication;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

    // Firebase authentication instance
    private FirebaseAuth auth;

    // UI elements
    private EditText signupEmail, signupPassword, signupUsername, signupDOB, signupCountry;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance(); // Initialize Firebase authentication

        // Bind UI elements with XML
        signupEmail = findViewById(R.id.email);
        signupPassword = findViewById(R.id.password);
        signupUsername = findViewById(R.id.username_register);
        signupDOB = findViewById(R.id.DOB_register);
        signupCountry = findViewById(R.id.Country_register);
        signupButton = findViewById(R.id.btn_register);
        loginRedirectText = findViewById(R.id.loginNow);

        // Show DatePicker dialog when 'Enter' key is pressed on password field
        signupPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // If Enter key is pressed and it's an action_down event
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Show DatePicker dialog
                    showDatePickerDialog();
                    return true;
                }
                return false;
            }
        });

        // Register the user when the signup button is clicked
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate and process user input
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
                            TestFirestoreActivity.addDataToFirestore(uid, username, country, dob, 0,0,0,0,0,0,0,0,0, 0, 0, false, false, false);
                            startActivity(new Intent(Register.this, Login.class));
                        } else {
                            Toast.makeText(Register.this, "Failed to Register" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Redirect to login activity when 'Login Now' text is clicked
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        // Trigger signupButton click listener action when click on field
        signupDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) { // If the input field gains focus
                    String dob = signupDOB.getText().toString().trim();
                    if (dob.isEmpty()) {
                        // If field input is empty, show the date picker dialog
                        showDatePickerDialog();
                    }
                }
            }
        });

        // Trigger signupButton click listener action when 'Enter' key is pressed on country field
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

    // Method to display DatePicker dialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format day and month with leading zeros if needed
                        String dayOfMonthString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String monthOfYearString = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        // Set the selected date to the signupDOB EditText
                        String dateOfBirth = dayOfMonthString + "/" + monthOfYearString + "/" + year;
                        signupDOB.setText(dateOfBirth);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to validate date format (DD/MM/YYYY)
    private boolean isValidDateFormat(String dob) {
        // Regular expression to match DD/MM/YYYY format
        String regex = "\\d{2}/\\d{2}/\\d{4}";
        return dob.matches(regex);
    }

    // Method to validate country name
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