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

package com.teamten.visionfit.java;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.annotation.KeepName;
import com.teamten.visionfit.CameraSource;
import com.teamten.visionfit.CameraSourcePreview;
import com.teamten.visionfit.GraphicOverlay;
import com.teamten.visionfit.R;
import com.teamten.visionfit.java.posedetector.PoseDetectorProcessor;
import com.teamten.visionfit.java.posedetector.classification.PoseClassifierProcessor;
import com.teamten.visionfit.preference.PreferenceUtils;
import com.teamten.visionfit.preference.SettingsActivity;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Live preview visionfit for ML Kit APIs. */
@KeepName
public final class LivePreviewActivity extends AppCompatActivity
    implements OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

  private static final String POSE_DETECTION = "Pose Detection";

  private static final String TAG = "LivePreviewActivity";

  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = POSE_DETECTION;

  private TextView repCountText;
  private String repCountStr;
  //exerciseTypeStr and classType should be the same, will need to do comparisons between them to check for matching classes
  private String exerciseTypeStr;
  public static String classType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_vision_live_preview);

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



//    Spinner spinner = findViewById(R.id.spinner);
//    List<String> options = new ArrayList<>();
//    options.add(POSE_DETECTION);
//
//    // Creating adapter for spinner
//    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
//    // Drop down layout style - list view with radio button
//    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    // attaching data adapter to spinner
//    spinner.setAdapter(dataAdapter);
//    spinner.setOnItemSelectedListener(this);

    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

//    ImageView settingsButton = findViewById(R.id.settings_button);
//    settingsButton.setOnClickListener(
//        v -> {
//          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
//          intent.putExtra(
//              SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW);
//          startActivity(intent);
//        });

    createCameraSource(selectedModel);


    //Get the rep counter to update every 0.2 seconds
    repCountText = findViewById(R.id.exercise_count_text);
    repCountText.setText(classType+"\nRep:0");

    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
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
                    repCountText.setText(exerciseTypeStr+"\nReps:"+repCountStr);
                  } else if (classType.equals(exerciseTypeStr)) {
                    repCountText.setText(exerciseTypeStr+"\nReps:"+repCountStr);
                  }
                } else {
                  Log.d("MyMessage", "rep count and/or exercise type is null");
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
      switch (model) {

        case POSE_DETECTION:
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
          break;

        default:
          Log.e(TAG, "Unknown model: " + model);
      }
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
    if (cameraSource != null) {
      cameraSource.release();
    }
  }
}
