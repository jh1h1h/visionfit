package Team10_VisionFit.Backend.firebaseAuthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.teamten.visionfit.R;
import Team10_VisionFit.MainActivity;

public class Login extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;
    private FirebaseAuth auth;
    TextView forgotPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean savelogin;
    CheckBox savelogindetails;


    public void loginFunc(){
        String email = loginEmail.getText().toString();
        String pass = loginPassword.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                if(savelogindetails.isChecked()) {
                                    editor.putBoolean("savelogin", true);
                                    editor.putString("email", email);
                                    editor.putString("password", pass);
                                    editor.commit();
                                }
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Login Failed. Invalid email and/or password.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                loginPassword.setError("Email cannot be empty");
            }
        } else if (email.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
        } else {
            loginEmail.setError("Please enter correct email");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);

        loginButton = findViewById(R.id.btn_login);
        signupRedirectText = findViewById(R.id.registerNow);
        forgotPassword = findViewById(R.id.txtForgotPassword);
        auth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        savelogindetails = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        editor = sharedPreferences.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginFunc();
            }
        });
        savelogin=sharedPreferences.getBoolean("savelogin", true);
        if(savelogin==true) {
            loginEmail.setText(sharedPreferences.getString("email", null));
            loginPassword.setText(sharedPreferences.getString("password", null));
        }

        loginPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginFunc();
                    return true;
                }
                return false;
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(Login.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this, "Unable to send reset email, failed to reset password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }
}