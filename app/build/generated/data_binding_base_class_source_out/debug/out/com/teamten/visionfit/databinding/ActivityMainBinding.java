// Generated by view binder compiler. Do not edit!
package com.teamten.visionfit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamten.visionfit.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigation;

  @NonNull
  public final TextView bottomText;

  @NonNull
  public final Button cameraButton;

  @NonNull
  public final AppCompatButton dailychallengeButton;

  @NonNull
  public final ConstraintLayout greeting;

  @NonNull
  public final AppCompatButton leaderboardButton;

  @NonNull
  public final AppCompatButton mystatsButton;

  @NonNull
  public final AppCompatButton rewardsButton;

  @NonNull
  public final ConstraintLayout streakMessage;

  @NonNull
  public final TextView userDetails;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavigation, @NonNull TextView bottomText,
      @NonNull Button cameraButton, @NonNull AppCompatButton dailychallengeButton,
      @NonNull ConstraintLayout greeting, @NonNull AppCompatButton leaderboardButton,
      @NonNull AppCompatButton mystatsButton, @NonNull AppCompatButton rewardsButton,
      @NonNull ConstraintLayout streakMessage, @NonNull TextView userDetails) {
    this.rootView = rootView;
    this.bottomNavigation = bottomNavigation;
    this.bottomText = bottomText;
    this.cameraButton = cameraButton;
    this.dailychallengeButton = dailychallengeButton;
    this.greeting = greeting;
    this.leaderboardButton = leaderboardButton;
    this.mystatsButton = mystatsButton;
    this.rewardsButton = rewardsButton;
    this.streakMessage = streakMessage;
    this.userDetails = userDetails;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottomNavigation;
      BottomNavigationView bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }

      id = R.id.bottomText;
      TextView bottomText = ViewBindings.findChildViewById(rootView, id);
      if (bottomText == null) {
        break missingId;
      }

      id = R.id.cameraButton;
      Button cameraButton = ViewBindings.findChildViewById(rootView, id);
      if (cameraButton == null) {
        break missingId;
      }

      id = R.id.dailychallengeButton;
      AppCompatButton dailychallengeButton = ViewBindings.findChildViewById(rootView, id);
      if (dailychallengeButton == null) {
        break missingId;
      }

      id = R.id.greeting;
      ConstraintLayout greeting = ViewBindings.findChildViewById(rootView, id);
      if (greeting == null) {
        break missingId;
      }

      id = R.id.leaderboardButton;
      AppCompatButton leaderboardButton = ViewBindings.findChildViewById(rootView, id);
      if (leaderboardButton == null) {
        break missingId;
      }

      id = R.id.mystatsButton;
      AppCompatButton mystatsButton = ViewBindings.findChildViewById(rootView, id);
      if (mystatsButton == null) {
        break missingId;
      }

      id = R.id.rewardsButton;
      AppCompatButton rewardsButton = ViewBindings.findChildViewById(rootView, id);
      if (rewardsButton == null) {
        break missingId;
      }

      id = R.id.streakMessage;
      ConstraintLayout streakMessage = ViewBindings.findChildViewById(rootView, id);
      if (streakMessage == null) {
        break missingId;
      }

      id = R.id.user_details;
      TextView userDetails = ViewBindings.findChildViewById(rootView, id);
      if (userDetails == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, bottomNavigation, bottomText,
          cameraButton, dailychallengeButton, greeting, leaderboardButton, mystatsButton,
          rewardsButton, streakMessage, userDetails);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
