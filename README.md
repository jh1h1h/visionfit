VisionFit 2.0

⭐New features
✅Profile Page (Dummy ver)

✅Countdown for exercises working (refer to video demo) --> once users press start, countdown will begin and once the countdown ends they will be redirected back to daily challenges page AUTOMATICALLY

✅When logging out, users wil be prompted a confirmation (I totally didnt log out by accident too many times)

✅Added scrollbars to all pages with scrollview

✅Privacy Policy Page --> added fluff onto edgar's fluff

✅Check Pose/ Camera button added to nav bar  --> help me test if can launch from every page when yall pull

✅Changed icons to more consistent looking ones 

✅Buttons in navbar will light up if you at the particular page ( i didnt do this though so idk how)

✅All buttons have a logcat feature tagged to "Button Check" to detect if its being clicked or not --> go to logcat and search for "Button Check" for testing



📖Renaming and Directory Changing
Java
⚪All Java classes are now in src/main/java/Team10_VisionFit and within this package
⚪all app UI in "UI" package
⚪all firebase stuff in "Backend" package
⚪all ML kit stuff in "PoseDetector" package

⚪merged Edgar's firebase's mainactivity into MainActivity, now we only have 1 MainActivity (no duplicates)
⚪deleted all of the og ML kit stuffs like SettingsActivity (we have one too but now no more duplicates)

Res
⚪deleted all old ML kit UI stuff in res as well as all the default stuff when a new project is created --> should have no traces of ML kit UI
⚪ deleted all duplcates of icons and logos
⚪ visionfit logo should only be found in mipmap
⚪renamed all icons to start with "icon"
⚪ renamed all exercise icons to start with "exercise"

❌Missing Features + Things we need discuss
⚪Leaderboard (to show daily and cumulative)--> work in progress
⚪Profile Page havent link to firebase
⚪Reward System How? --> need discuss
⚪About Us page --> not important but maybe put a pic of 7 of us






























































# ML Kit Vision Quickstart Sample App

## Introduction

This ML Kit Quickstart app demonstrates how to use and integrate various vision based ML Kit features into your app.

## Feature List

Features that are included in this Quickstart app:
* [Object Detection](https://developers.google.com/ml-kit/vision/object-detection/android) - Detect, track, and classify objects in real time and static images.
* [Face Detection](https://developers.google.com/ml-kit/vision/face-detection/android) - Detect faces in real time and static images.
* [Face Mesh Detection](https://developers.google.com/ml-kit/vision/face-mesh-detection/android) - Detect face mesh in real time and static images.
* [Text Recognition](https://developers.google.com/ml-kit/vision/text-recognition/android) - Recognize text in real time and static images.
* [Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning/android)  - Scan barcodes in real time and static images.
* [Image Labeling](https://developers.google.com/ml-kit/vision/image-labeling/android) - Label images in real time and static images.
* [Custom Image Labeling - Birds](https://developers.google.com/ml-kit/vision/image-labeling/custom-models/android) - Label images of birds with a custom TensorFlow Lite model.
* [Pose Detection](https://developers.google.com/ml-kit/vision/pose-detection/android) - Detect the position of the human body in real time.
* [Selfie Segmentation](https://developers.google.com/ml-kit/vision/selfie-segmentation/android) - Segment people from the background in real time.
* [Subject Segmentation](https://developers.google.com/ml-kit/vision/subject-segmentation/android) - Segment multiple subjects from the background in static images.

<img src="../screenshots/quickstart-picker.png" width="220"/> <img src="../screenshots/quickstart-image-labeling.png" width="220"/> <img src="../screenshots/quickstart-object-detection.png" width="220"/> <img src="../screenshots/quickstart-pose-detection.png" width="220"/>

## Getting Started

* Run the sample code on your Android device or emulator
* Try extending the code to add new features and functionality

## How to use the app

This app supports three usage scenarios: Live Camera, Static Image, and CameraX enabled live camera.

### Live Camera scenario
It uses the camera preview as input and contains these API workflows: Object detection & tracking, Face Detection, Face Mesh Detection, Text Recognition, Barcode Scanning, Image Labeling, and Pose Detection. There's also a settings page that allows you to configure several options:
* Camera
    * Preview size - Specify the preview size of rear/front camera manually (Default size is chosen appropriately based on screen size)
    * Enable live viewport - Toggle between blocking camera preview by API processing and result rendering or not
* Object detection / Custom Object Detection
    * Enable multiple objects -- Enable multiple objects to be detected at once
    * Enable classification -- Enable classification for each detected object
* Face Detection
    * Landmark mode -- Toggle between showing no or all facial landmarks
    * Contour mode -- Toggle between showing no or all contours
    * Classification mode -- Toggle between showing no or all classifications (smiling, eyes open/closed)
    * Performance mode -- Toggle between two operating modes (Fast or Accurate)
    * Face tracking -- Enable or disable face tracking
    * Minimum face size -- Choose the proportion of the head width to the image width
* Face Mesh Detection
    * Use Case -- Selects from `Bounding Box only` and `Face Mesh`
* Pose Detection
    * Performance mode -- Allows you to switch between "Fast" and "Accurate" operation mode
    * Show in-frame likelihood -- Displays InFrameLikelihood score for each landmark
    * Visualize z value -- Uses different colors to indicate z difference (red: smaller z, blue: larger z)
    * Rescale z value for visualization -- Maps the smallest z value to the most red and the largest z value to the most blue. This makes z difference more obvious
    * Run classification -- Classify squat and pushup poses. Count reps in streaming mode.
* Selfie Segmentation
    * Enable raw size mask -- Asks the segmenter to return the raw size mask which matches the model output size.

### Static Image scenario
The static image scenario is identical to the live camera scenario, but instead relies on images fed into the app through the gallery.

### CameraX Live Preview scenario
The CameraX live preview scenario is very similar to the native live camera scenario, but instead relies on CameraX live preview. Note: CameraX is only supported on API level 21+.

## Support

* [Documentation](https://developers.google.com/ml-kit/guides)
* [API Reference](https://developers.google.com/ml-kit/reference/android)
* [Stack Overflow](https://stackoverflow.com/questions/tagged/google-mlkit)

## License

Copyright 2020 Google, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
