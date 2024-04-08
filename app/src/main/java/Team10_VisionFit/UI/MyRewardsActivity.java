package Team10_VisionFit.UI;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamten.visionfit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRewardsActivity extends BaseActivity {
    FirebaseAuth auth;
    boolean hasCompletedSquatsChallengeToday;
    boolean hasCompletedPushupsChallengeToday;
    boolean streakChange;
    int current_points;
    int lifetime_points;
    int rewards_1_fb;
    int rewards_2_fb;
    int rewards_3_fb;
    int rewards_4_fb;
    String purchase_history;
    TextView my_points_now;
    TextView my_total_points;
    ConstraintLayout rewards_1;
    ConstraintLayout rewards_2;
    ConstraintLayout rewards_3;
    ConstraintLayout rewards_4;
    List<String> purchaseHistory = new ArrayList<>(); // List to store purchase history
    Button purchaseHistoryButton;
    Button myVouchers;
    Map<String, Integer> redeemedRewards = new HashMap<>();
    TextView rewards1_rep;
    TextView rewards2_rep;
    TextView rewards3_rep;
    TextView rewards4_rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rewards);

        my_points_now = findViewById(R.id.earnings);
        my_total_points = findViewById(R.id.lifettime_earnings);
        rewards_1 = findViewById(R.id.constraint_1);
        rewards_2 = findViewById(R.id.constraint_2);
        rewards_3 = findViewById(R.id.constraint_3);
        rewards_4 = findViewById(R.id.constraint_4);
        purchaseHistoryButton = findViewById(R.id.rewards_history_button);
        myVouchers = findViewById(R.id.redeem_button);

        //Getting the user's number of completed challenges to decide on the new challenge, and whether the user has completed today's challenges
        auth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

        //The task is done ASYNCHRONOUSLY, hence need to put everything inside
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    //Get the current status of whether challenge has been completed and total challenges completed
                    hasCompletedSquatsChallengeToday = document.getBoolean("hasCompletedSquatsChallengeToday");
                    hasCompletedPushupsChallengeToday = document.getBoolean("hasCompletedPushupsChallengeToday");
                    streakChange = document.getBoolean("streakChange");
                    current_points = document.getLong("current_points").intValue();
                    lifetime_points = document.getLong("lifetime_points").intValue();
                    rewards_1_fb = document.getLong("num_rewards1").intValue();
                    rewards_2_fb = document.getLong("num_rewards2").intValue();
                    rewards_3_fb = document.getLong("num_rewards3").intValue();
                    rewards_4_fb = document.getLong("num_rewards4").intValue();

                    Object purchaseHistoryObj = document.get("purchaseHistory");
                    if (purchaseHistoryObj instanceof ArrayList) {
                        purchaseHistory = (ArrayList<String>) purchaseHistoryObj;
                    } else {
                        Log.e("MyRewardsActivity", "purchaseHistory is not stored as ArrayList in Firestore");
                        // Handle this situation accordingly
                    }
                    if (purchaseHistory == null) {
                        purchaseHistory = new ArrayList<>();
                    }

                    // Set the text of TextViews after updating points
                    my_points_now.setText(String.valueOf(current_points));
                    my_total_points.setText(String.valueOf(lifetime_points));
                }

                //To setup nav bar
                setUpBottomNavBar(R.id.bottom_logout);

                rewards_1.setOnClickListener(v -> {
                    Log.d("Button Check", "Rewards 1 Clicked");
                    confirmPurchaseDialog(10000, "3 Days Data Roaming"); // Deduct 10000 points for rewards_1
                });

                rewards_2.setOnClickListener(v -> {
                    Log.d("Button Check", "Rewards 2 Clicked");
                    confirmPurchaseDialog(7500, "1 Day Data Roaming"); // Deduct 7500 points for rewards_2
                });

                rewards_3.setOnClickListener(v -> {
                    Log.d("Button Check", "Rewards 3 Clicked");
                    confirmPurchaseDialog(5000, "$5 Phone Bill Voucher"); // Deduct 5000 points for rewards_3
                });

                rewards_4.setOnClickListener(v -> {
                    Log.d("Button Check", "Rewards 4 Clicked");
                    confirmPurchaseDialog(3000, "$3 Phone Bill Voucher"); // Deduct 3000 points for rewards_4
                });

                purchaseHistoryButton.setOnClickListener(v -> {
                    displayPurchaseHistory();
                });

                myVouchers.setOnClickListener(v -> {
                    displayRedeemedRewards();
                });
            }
        });
    }

    // Method to update purchase history in Firestore
    private void updatePurchaseHistoryInFirestore(List<String> updatedPurchaseHistory) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        userRef.update("purchaseHistory", updatedPurchaseHistory)
                .addOnSuccessListener(aVoid -> Log.d("MyRewardsActivity", "Purchase history updated successfully"))
                .addOnFailureListener(e -> Log.e("MyRewardsActivity", "Error updating purchase history", e));
    }

    // Method to add purchase to purchase history and update in Firestore
    private void addToPurchaseHistoryAndUpdateFirestore(String rewardName) {
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        purchaseHistory.add(rewardName);
        updatePurchaseHistoryInFirestore(purchaseHistory);
    }

    public void displayRedeemedRewards() {
        // Creating a custom dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_redeemed_rewards);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Getting references to TextViews in dialog layout
        rewards1_rep = dialog.findViewById(R.id.reward1_count);
        rewards2_rep = dialog.findViewById(R.id.reward2_count);
        rewards3_rep = dialog.findViewById(R.id.reward3_count);
        rewards4_rep = dialog.findViewById(R.id.reward4_count);

        // Set text for TextViews
        rewards1_rep.setText(String.valueOf(rewards_1_fb));
        rewards2_rep.setText(String.valueOf(rewards_2_fb));
        rewards3_rep.setText(String.valueOf(rewards_3_fb));
        rewards4_rep.setText(String.valueOf(rewards_4_fb));

        // Getting reference to Close button in dialog layout
        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss the dialog when Close button is clicked
        });

        // Set dialog to be non-cancelable outside of button click
        dialog.setCancelable(false);
        // Showing the dialog
        dialog.show();
    }


    public void confirmPurchaseDialog(final int pointsToDeduct, final String rewardName) {
        // creating custom dialog
        final Dialog dialog = new Dialog(MyRewardsActivity.this);

        // setting content view to dialog
        dialog.setContentView(R.layout.dialog_confirmation);

        // getting reference of TextView
        TextView dialogButtonYes = dialog.findViewById(R.id.textViewYes);
        TextView dialogButtonNo = dialog.findViewById(R.id.textViewNo);

        // click listener for No
        dialogButtonNo.setOnClickListener(v -> {
            // dismiss the dialog
            dialog.dismiss();
        });

        // click listener for Yes
        dialogButtonYes.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current logged in User's ID
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); // Using that ID, get the user's data from firestore

            // dismiss the dialog and process the purchase
            dialog.dismiss();

            // Check if the user has sufficient points to make the purchase
            if (current_points >= pointsToDeduct) {
                // Deduct points from current_points in Firestore
                current_points -= pointsToDeduct;
                userRef.update("current_points", current_points)
                        .addOnSuccessListener(aVoid -> {
                            // Points deducted successfully
                            my_points_now.setText(String.valueOf(current_points));
                            Toast.makeText(getApplicationContext(), "Points deducted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Failed to deduct points
                            Toast.makeText(getApplicationContext(), "Failed to deduct points", Toast.LENGTH_SHORT).show();
                        });

                // Update Firestore with the purchased reward
                addToPurchaseHistoryAndUpdateFirestore(rewardName);

            } else {
                // Insufficient points
                Toast.makeText(getApplicationContext(), "Failed to purchase. Insufficient points", Toast.LENGTH_SHORT).show();
            }

            if (rewardName == "3 Days Data Roaming") {
                rewards_1_fb ++;
                userRef.update("num_rewards1", rewards_1_fb);
            } else if (rewardName == "1 Day Data Roaming") {
                rewards_2_fb ++;
                userRef.update("num_rewards2", rewards_2_fb);
            } else if (rewardName == "$5 Phone Bill Voucher") {
                rewards_3_fb ++;
                userRef.update("num_rewards3", rewards_3_fb);
            } else if (rewardName == "$3 Phone Bill Voucher") {
                rewards_4_fb ++;
                userRef.update("num_rewards4", rewards_4_fb);
            }
        });

        // show the exit dialog
        dialog.show();
    }

    // Method to display purchase history in reverse order
    public void displayPurchaseHistory() {
        // Creating a custom dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_purchase_history);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Getting reference to RecyclerView in dialog layout
        RecyclerView recyclerView = dialog.findViewById(R.id.purchase_history_recycler_view);

        // Reverse the purchase history list
        List<String> reversedPurchaseHistory = new ArrayList<>(purchaseHistory);
        Collections.reverse(reversedPurchaseHistory);

        // Setting up RecyclerView
        PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(reversedPurchaseHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting reference to Close button in dialog layout
        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss the dialog when Close button is clicked
        });

        // Set dialog to be non-cancelable outside of button click
        dialog.setCancelable(false);
        // Showing the dialog
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        // Do nothing (disable back button functionality)
    }
}

