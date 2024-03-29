package Team10_VisionFit.Backend.firebaseCloudFireStore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teamten.visionfit.R;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if notifications are enabled
        if (isNotificationEnabled()) {
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            // Create notification channel
            createNotificationChannel();

            // Use the correct channelId when building the notification
            final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.visionfitlogo_round)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, notificationBuilder.build());
        }
    }

    // Method to create notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name"; // Replace with your desired channel name
            String description = "Channel Description"; // Replace with your desired channel description
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("HEADS_UP_NOTIFICATION", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isNotificationEnabled() {
        // Retrieve the switch state from SharedPreferences
        // Replace "notificationSwitchState" with your preference key
        return getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getBoolean("notificationSwitchState", true); // Default value is true
    }

    // Method to update the notification state in SharedPreferences
    private void setNotificationEnabled(boolean isEnabled) {
        // Save the switch state to SharedPreferences
        // Replace "notificationSwitchState" with your preference key
        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("notificationSwitchState", isEnabled)
                .apply();
    }
}
