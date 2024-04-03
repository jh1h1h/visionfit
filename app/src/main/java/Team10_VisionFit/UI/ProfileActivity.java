package Team10_VisionFit.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.teamten.visionfit.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import Team10_VisionFit.Backend.firebaseAuthentication.Login;
import Team10_VisionFit.MainActivity;
import Team10_VisionFit.PoseDetector.LivePreviewActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

    private CircleImageView profileImageView;
    private TextView displayNameTextView;
    private EditText editName;
    private EditText editEmail;
    private EditText editPw;
    private EditText editDob;
    private EditText editCountry;
    private FirebaseFirestore firestore;
    private Uri photoUri;
    private AppCompatButton saveChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();

        profileImageView = findViewById(R.id.userImage);
        displayNameTextView = findViewById(R.id.username_profile);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editDob = findViewById(R.id.edit_DOB);
        editCountry = findViewById(R.id.edit_country);
        saveChanges = findViewById(R.id.save_changes_button);
        ImageView takePhotoButton = findViewById(R.id.edit_profile_pic);

        takePhotoButton.setOnClickListener(v -> {
            // Show dialog to choose between camera and gallery
            showChooseImageSourceDialog();
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            String email = user.getEmail();

            DocumentReference userRef = firestore.collection("users").document(userId);

            // Fetch user data from Firestore
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Get profile picture URL and display name
                        String profilePicUrl = document.getString("photo_url");
                        String displayName = document.getString("username");
                        String country = document.getString("country");
                        String dob = document.getString("dob");

                        displayNameTextView.setText(displayName);
                        editName.setText(displayName);
                        editEmail.setText(email);
                        editCountry.setText(country);
                        editDob.setText(dob);

                        if (profilePicUrl != null) {
                            // Load profile picture using Picasso with error handling
                            Picasso.get().load(profilePicUrl).into(profileImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // Image loaded successfully, do nothing
                                }

                                @Override
                                public void onError(Exception e) {
                                    // Error loading image, set default image
                                    profileImageView.setImageResource(R.drawable.icon_profile);
                                }
                            });
                        } else {
                            // User doesn't have a profile picture, set default image
                            profileImageView.setImageResource(R.drawable.icon_profile);
                        }
                    }
                }
            });
        }

        saveChanges.setOnClickListener(v -> {
            // Get the updated user data from input fields
            String username = editName.getText().toString();
            String email = editEmail.getText().toString();
            String dob = editDob.getText().toString();
            String country = editCountry.getText().toString();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Create a map to update user data in Firestore
            Map<String, Object> updatedUserData = new HashMap<>();
            updatedUserData.put("username", username);
            updatedUserData.put("email", email);
            updatedUserData.put("dob", dob);
            updatedUserData.put("country", country);

            // Update user data in Firestore
            DocumentReference userRef = firestore.collection("users").document(userId);
            userRef.update(updatedUserData)
                    .addOnSuccessListener(aVoid -> {
                        // User data updated successfully
                        Toast.makeText(ProfileActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        Log.d("Button Check", "Save Changes Button Clicked");
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update user data
                        Toast.makeText(ProfileActivity.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileActivity", "Error updating user data", e);
                    });
        });

        initBottomNavigation();
    }

    // Method to show dialog for choosing image source (camera or gallery)
    private void showChooseImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Camera option chosen
                    checkCameraPermissionAndOpenCamera();
                    break;
                case 1:
                    // Gallery option chosen
                    openGallery();
                    break;
            }
        });
        builder.show();
    }

    // Method to check camera permission and open camera
    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, start camera intent
            openCamera();
        } else {
            // Request camera and storage permissions
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    // Method to open the gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Method to start camera intent
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                // Photo captured from camera successfully
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Get the captured photo
                    Bitmap photoBitmap = (Bitmap) extras.get("data");
                    if (photoBitmap != null) {
                        // Convert bitmap to URI
                        photoUri = getImageUri(photoBitmap);
                        if (photoUri != null) {
                            // Update user profile photo URL in Firestore
                            updateUserProfilePhoto(photoUri.toString());
                        }
                    }
                }
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                // Photo selected from gallery successfully
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Update user profile photo URL in Firestore
                    updateUserProfilePhoto(selectedImageUri.toString());
                }
            }
        }

    }

    // Method to convert bitmap to URI
    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    // Method to update user profile photo URL in Firestore
    private void updateUserProfilePhoto(String photoUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = firestore.collection("users").document(userId);

            // Create a map to update user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("photo_url", photoUrl);

            // Update user data in Firestore
            userRef.set(userData, SetOptions.merge())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Profile photo URL updated successfully
                            // Display the updated photo using Picasso
                            Picasso.get().load(photoUrl).into(profileImageView);
                        } else {
                            // Failed to update profile photo URL
                            profileImageView.setImageResource(R.drawable.icon_profile);
                            Toast.makeText(getApplicationContext(), "Failed to update profile photo", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        SharedPreferences.Editor editor = getSharedPreferences("UserProfile", MODE_PRIVATE).edit();
        editor.putString("profileImageUri", photoUrl);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Camera and storage permissions granted, start camera intent
                openCamera();
            } else {
                // Permission denied, display error message
                Toast.makeText(this, "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_userProfile);
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
        final Dialog dialog = new Dialog(ProfileActivity.this);

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

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}
