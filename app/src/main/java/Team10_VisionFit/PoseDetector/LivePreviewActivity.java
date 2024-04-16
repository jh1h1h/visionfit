/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Team10_VisionFit.PoseDetector;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.teamten.visionfit.R;

import java.io.IOException;

import Team10_VisionFit.Backend.preference.PreferenceUtils;
import Team10_VisionFit.PoseDetector.classification.PoseClassifierProcessor;
import Team10_VisionFit.PoseDetector.classification.RepetitionCounter;
import Team10_VisionFit.UI.DailyChallengeActivity;

@KeepName
public final class LivePreviewActivity extends AppCompatActivity
    implements OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

  FirebaseAuth auth;
  int numSquatsChallengeCompleted;
  int numPushupsChallengeCompleted;
  long countdownDurationMillis = 10000;
  private CountDownTimer countdownTimer;
  long countdownSquats;
  long countdownPushups;

  private static final String POSE_DETECTION = "Pose Detection";
  private static final String TAG = "LivePreviewActivity";
  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = POSE_DETECTION;

  private TextView repCountText;
  private String repCountStr;
  private String prevRepCountStr;
  
  //exerciseTypeStr and classType should be the same, will need to do comparisons between them to check for matching classes
  private String exerciseTypeStr;
  public static String classType;
  public int counter;
  private TextView timerText;
  private TextToSpeech tts;
  private Handler handler = new Handler();
  private Runnable mainCountdownRunnable;
  private long endTime;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_vision_live_preview);

    // TIMING STUFF

    timerText =  findViewById(R.id.timer_text);
    Button startButton = findViewById(R.id.start_button);
    // Set startButton as disabled initially
    startButton.setEnabled(true);

    //Getting the user's number of completed challenges to decide on the new challenge, and whether the user has completed today's challenges
    auth = FirebaseAuth.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //Get the current logged in User's ID
    DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid); //Using that ID, get the user's data from firestore

    userRef.get().addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                  //Get the current status of whether challenge has been completed and total challenges completed
                  numSquatsChallengeCompleted = document.getLong("numSquatsChallengeCompleted").intValue();
                  numPushupsChallengeCompleted = document.getLong("numPushupsChallengeCompleted").intValue();

                  //The idea is that every 5 days, increment the timer by 2s
                  countdownSquats = countdownDurationMillis + ((numSquatsChallengeCompleted / 5) * 2000L); //the divide will give an int, so its basically integer divide, then I multiply that by 2 because I want to add 2 to each increment of 5 days
                  countdownPushups = countdownDurationMillis + ((numPushupsChallengeCompleted / 5) * 2000L); //the divide will give an int, so its basically integer divide, then I multiply that by 2 because I want to add 2 to each increment of 5 days
                  // Enable the start button after user data is retrieved
                  startButton.setEnabled(true);
                }
              }
            });

    startButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("Button Check", "Start Button Clicked");
        // Disable the start button to prevent multiple clicks
        startButton.setEnabled(false);
        //resetRepCounters(); //Reset counters to 0 when start is pressed.
        //PoseClassifierProcessor.repCountForText = "0"; //Reset the text as well so it reflects immediately at start

        if (classType.equals("Push Ups")) {
          startCountdownTimer(countdownPushups);
        } else if (classType.equals("Squats")) {
          startCountdownTimer(countdownSquats);
        }
      }
    });

    //Get intent to retrieve class type (determine which entrypoint)
    Intent intent = getIntent();
    classType = intent.getStringExtra("ClassType");

    preview = findViewById(R.id.preview_view);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    createCameraSource(selectedModel);


    //Get the rep counter to update every 0.2 seconds
    repCountText = findViewById(R.id.exercise_count_text);
    repCountText.setText(classType+"\nRep:0");
    PoseClassifierProcessor.repCountForText = "0"; //Reset the text as well so it reflects immediately at start

    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
          resetRepCounters();
          while (!isInterrupted()) {
            Thread.sleep(200);
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  repCountStr = PoseClassifierProcessor.repCountForText;
                  exerciseTypeStr = PoseClassifierProcessor.exerciseTypeForText;
                  if ((repCountStr != null) && (exerciseTypeStr != null)) {
                      //If they are doing a specific type of exercise (not freestyle), then only consider reps of that exercise
                      if (classType.equals("Free Style")) {
                          repCountText.setText(exerciseTypeStr + "\nReps:" + repCountStr);
                      } else if (classType.equals(exerciseTypeStr)) {
                          repCountText.setText(exerciseTypeStr + "\nReps:" + repCountStr);
//                          Log.d("cyril", repCountStr);
                      }
                  } else {
                      //Log.d("MyMessage", "rep count and/or exercise type is null, This is NORMAL at start up, because the model has not been loaded in yet");
                  }
              }
            });
          }
        } catch (InterruptedException ex) {
          repCountText.setText(classType);
        }
      }
    };
    thread.start();

    // Initialize TextToSpeech
    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          // TextToSpeech initialization successful
        } else {
          // TextToSpeech initialization failed
          Log.e(TAG, "TextToSpeech initialization failed");
        }
      }
    });

    Button exitButton = findViewById(R.id.Exit_button);
    exitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Cancel the countdown timer if it's running
        if (countdownTimer != null) {
          countdownTimer.cancel();
        }

        Intent intent2 = new Intent(LivePreviewActivity.this, DailyChallengeActivity.class);
        //Get the number of reps completed for the particular exercise
        String label = classType + " Reps";
        intent2.putExtra(label,"0"); //If user quit by exiting, means forfeit the challenge, so 0
        resetRepCounters(); //Reset reps to zero

        startActivity(intent2);
        Log.d("Button Check", "Exit Button Clicked");
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
      }
    });
  }

  private void startCountdownTimer(long durationMillis) {
    new CountDownTimer(3000, 1000) {
      int countdownValue = 3; // Starting from 3

      public void onTick(long millisUntilFinished) {
        Log.d(TAG, "Initial countdown tick: " + countdownValue + " seconds remaining");
        timerText.setTextColor(Color.RED);
        timerText.setText(String.valueOf(countdownValue));
        speakCountdown(String.valueOf(countdownValue));
        countdownValue--;
      }

      public void onFinish() {
        Log.d(TAG, "Initial countdown finished, starting main countdown");
        timerText.setTextColor(Color.GREEN);
        timerText.setText("START");
        speakStart();
        handler.postDelayed(() -> startMainCountdown(durationMillis), 1000);

      }
    }.start();
  }

  private void startMainCountdown(long durationMillis) {
    // Start with a slight delay to compensate for any immediate overhead
    endTime = SystemClock.elapsedRealtime() + durationMillis + 100;
    resetRepCounters();

    mainCountdownRunnable = new Runnable() {
      @Override
      public void run() {
        long millisUntilFinished = endTime - SystemClock.elapsedRealtime();
        if (millisUntilFinished > 0) {
          long secondsRemaining = millisUntilFinished / 1000;
          long minutes = secondsRemaining / 60;
          long seconds = secondsRemaining % 60;
          String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);

          timerText.setTextColor(Color.DKGRAY);
          timerText.setText(timeLeftFormatted);

          // Check if there are 3 seconds or less remaining and speak the countdown
          if (secondsRemaining <= 3 && secondsRemaining > 0) {
            speakCountdown(String.valueOf(secondsRemaining));
          } else if (secondsRemaining ==0){
            speakStop();
          }

          handler.postDelayed(this, 1000); // Schedule the next update after 1 second
        } else {
          handler.removeCallbacks(this);
          customEndChallengeDialog();
        }
      }
    };

    // Start the countdown immediately
    handler.post(mainCountdownRunnable);
  }


  // Method to speak "START"
  private void speakStart() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Use TextToSpeech API for Lollipop and above
      String utteranceId = this.hashCode() + "";
      tts.speak("START", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    } else {
      // For versions before Lollipop, speak without utterance ID
      tts.speak("START", TextToSpeech.QUEUE_FLUSH, null);
    }
  }

  // Method to speak "STOP"
  private void speakStop() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Use TextToSpeech API for Lollipop and above
      String utteranceId = this.hashCode() + "";
      tts.speak("STOP", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    } else {
      // For versions before Lollipop, speak without utterance ID
      tts.speak("STOP", TextToSpeech.QUEUE_FLUSH, null);
    }
  }

  // Method to speak the countdown value
  private void speakCountdown(String countdownValue) {
    // Parse the countdown value to an integer
    int value = Integer.parseInt(countdownValue);

    // Speak only if the countdown value is greater than 0
    if (value > 0) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // Use TextToSpeech API for Lollipop and above
        String utteranceId = this.hashCode() + "";
        tts.speak(countdownValue, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
      } else {
        // For versions before Lollipop, speak without utterance ID
        tts.speak(countdownValue, TextToSpeech.QUEUE_FLUSH, null);
      }
    }
  }

  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent.getItemAtPosition(pos).toString();
    Log.d(TAG, "Selected model: " + selectedModel);
    preview.stop();
    createCameraSource(selectedModel);
    startCameraSource();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      }
    }
    preview.stop();
    startCameraSource();
  }

  private void createCameraSource(String model) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
    }

    try {

        PoseDetectorOptionsBase poseDetectorOptions =
            PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
        Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
        boolean shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
        boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
        boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
        boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
        cameraSource.setMachineLearningFrameProcessor(
            new PoseDetectorProcessor(
                this,
                poseDetectorOptions,
                shouldShowInFrameLikelihood,
                visualizeZ,
                rescaleZ,
                runClassification,
                /* isStreamMode = */ true));
    } catch (RuntimeException e) {
      Log.e(TAG, "Can not create image processor: " + model, e);
      Toast.makeText(
              getApplicationContext(),
              "Can not create image processor: " + e.getMessage(),
              Toast.LENGTH_LONG)
          .show();
    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    createCameraSource(selectedModel);
    startCameraSource();
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    preview.stop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    // Clean up camera resources
    if (cameraSource != null) {
      cameraSource.release(); // Release the camera resource
      cameraSource = null; // Help garbage collector by nullifying the reference
    }

    // Clean up TextToSpeech resources
    if (tts != null) {
      tts.stop(); // Stop any ongoing TextToSpeech playback
      tts.shutdown(); // Release the TextToSpeech resources
      tts = null; // Nullify the reference to help the garbage collector
    }

    // Remove any callbacks from the handler to avoid memory leaks
    if (handler != null) {
      handler.removeCallbacksAndMessages(null);
    }
  }



  @Override
  public void onBackPressed() {
    // Do nothing to disable the back button functionality
  }

  public void resetRepCounters() {
    try {
      //To reset all rep counters to 0 when exit or when start is clicked
      for (RepetitionCounter repCounter : PoseClassifierProcessor.repCounters) {
//        Log.d("MyMessage", "Before: " + (repCounter.getNumRepeats()));
        repCounter.setNumRepeats(0);
//        Log.d("MyMessage", "After: " + (repCounter.getNumRepeats()));


      }
    } catch (NullPointerException e) {
      Log.d("MyMessage", "Pose Classifier is still loading, be patient");
    }

  }

  public  void customEndChallengeDialog() {
    // creating custom dialog
    final Dialog dialog = new Dialog(LivePreviewActivity.this);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    // setting content view to dialog
    dialog.setContentView(R.layout.dialog_finish_challenge);
    Button dialogButtonExit = dialog.findViewById(R.id.btnExit); // getting reference of Button
    TextView endChallenge_Text = dialog.findViewById(R.id.end_challenge_message); // getting reference of TextView

    // Calculate the rep count from repCountStr
    int repCount = Integer.parseInt(repCountStr);
    String end_Challenge_Text;

    if (classType.equals("Push Ups")) {
      end_Challenge_Text = "Good job!" + "\n" + "You have completed " + repCount + " push ups.";
    } else if (classType.equals("Squats")) {
      end_Challenge_Text = "Good job!" + "\n" + "You have completed " + repCount + " squats.";
    } else {
      end_Challenge_Text = "Challenge completed!";
    }

    // Set the text in the dialog
    endChallenge_Text.setText(end_Challenge_Text);

    // Speak the text
    speakText(end_Challenge_Text);

    // click listener for Yes
    dialogButtonExit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // dismiss the dialog and go to daily challenge UI
        dialog.dismiss();

        repCountText = findViewById(R.id.exercise_count_text);
        String[] repCountArr = String.valueOf(repCountText.getText()).split(":");
        // int count = Integer.parseInt(repCountArr[repCountArr.length - 1]);

        Intent intent = new Intent(LivePreviewActivity.this, DailyChallengeActivity.class);
        String label = classType + " Reps";
        intent.putExtra(label, repCountStr);
        intent.putExtra("repCount", repCount);
        intent.putExtra("ClassType", classType);
        resetRepCounters(); // Reset the counters to 0 before finish and exiting

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(intent);
        finish();
      }
    });
    // Set dialog to be non-cancelable outside of button click
    dialog.setCancelable(false);

    // show the exit dialog
    dialog.show();
  }

  // Method to speak the provided text
  private void speakText(String text) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Use TextToSpeech API for Lollipop and above
      String utteranceId = this.hashCode() + "";
      tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    } else {
      // For versions before Lollipop, speak without utterance ID
      tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
  }
}
