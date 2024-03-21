// Generated by view binder compiler. Do not edit!
package com.teamten.visionfit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamten.visionfit.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView PrivacyIcon;

  @NonNull
  public final ConstraintLayout Settings;

  @NonNull
  public final ImageView aboutUsIcon;

  @NonNull
  public final BottomNavigationView bottomNavigation;

  @NonNull
  public final ImageView changePasswordIcon;

  @NonNull
  public final ImageView editProfile;

  @NonNull
  public final ImageView nightModeIcon;

  @NonNull
  public final ImageView notificationIcon;

  @NonNull
  public final ImageView paymentIcon;

  @NonNull
  public final ScrollView scrollingSettings;

  @NonNull
  public final ImageView settingGoProfile;

  @NonNull
  public final ImageView settingGoResetPassword;

  @NonNull
  public final ConstraintLayout settingsContainer;

  @NonNull
  public final ImageView settingsGoAboutUs;

  @NonNull
  public final ImageView settingsGoPrivacy;

  @NonNull
  public final SwitchCompat switchMode;

  @NonNull
  public final ImageView userImage;

  @NonNull
  public final TextView usernameSettings;

  private ActivitySettingsBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView PrivacyIcon, @NonNull ConstraintLayout Settings,
      @NonNull ImageView aboutUsIcon, @NonNull BottomNavigationView bottomNavigation,
      @NonNull ImageView changePasswordIcon, @NonNull ImageView editProfile,
      @NonNull ImageView nightModeIcon, @NonNull ImageView notificationIcon,
      @NonNull ImageView paymentIcon, @NonNull ScrollView scrollingSettings,
      @NonNull ImageView settingGoProfile, @NonNull ImageView settingGoResetPassword,
      @NonNull ConstraintLayout settingsContainer, @NonNull ImageView settingsGoAboutUs,
      @NonNull ImageView settingsGoPrivacy, @NonNull SwitchCompat switchMode,
      @NonNull ImageView userImage, @NonNull TextView usernameSettings) {
    this.rootView = rootView;
    this.PrivacyIcon = PrivacyIcon;
    this.Settings = Settings;
    this.aboutUsIcon = aboutUsIcon;
    this.bottomNavigation = bottomNavigation;
    this.changePasswordIcon = changePasswordIcon;
    this.editProfile = editProfile;
    this.nightModeIcon = nightModeIcon;
    this.notificationIcon = notificationIcon;
    this.paymentIcon = paymentIcon;
    this.scrollingSettings = scrollingSettings;
    this.settingGoProfile = settingGoProfile;
    this.settingGoResetPassword = settingGoResetPassword;
    this.settingsContainer = settingsContainer;
    this.settingsGoAboutUs = settingsGoAboutUs;
    this.settingsGoPrivacy = settingsGoPrivacy;
    this.switchMode = switchMode;
    this.userImage = userImage;
    this.usernameSettings = usernameSettings;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.PrivacyIcon;
      ImageView PrivacyIcon = ViewBindings.findChildViewById(rootView, id);
      if (PrivacyIcon == null) {
        break missingId;
      }

      id = R.id.Settings;
      ConstraintLayout Settings = ViewBindings.findChildViewById(rootView, id);
      if (Settings == null) {
        break missingId;
      }

      id = R.id.aboutUsIcon;
      ImageView aboutUsIcon = ViewBindings.findChildViewById(rootView, id);
      if (aboutUsIcon == null) {
        break missingId;
      }

      id = R.id.bottomNavigation;
      BottomNavigationView bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }

      id = R.id.changePasswordIcon;
      ImageView changePasswordIcon = ViewBindings.findChildViewById(rootView, id);
      if (changePasswordIcon == null) {
        break missingId;
      }

      id = R.id.edit_profile;
      ImageView editProfile = ViewBindings.findChildViewById(rootView, id);
      if (editProfile == null) {
        break missingId;
      }

      id = R.id.nightModeIcon;
      ImageView nightModeIcon = ViewBindings.findChildViewById(rootView, id);
      if (nightModeIcon == null) {
        break missingId;
      }

      id = R.id.notificationIcon;
      ImageView notificationIcon = ViewBindings.findChildViewById(rootView, id);
      if (notificationIcon == null) {
        break missingId;
      }

      id = R.id.paymentIcon;
      ImageView paymentIcon = ViewBindings.findChildViewById(rootView, id);
      if (paymentIcon == null) {
        break missingId;
      }

      id = R.id.scrollingSettings;
      ScrollView scrollingSettings = ViewBindings.findChildViewById(rootView, id);
      if (scrollingSettings == null) {
        break missingId;
      }

      id = R.id.settingGoProfile;
      ImageView settingGoProfile = ViewBindings.findChildViewById(rootView, id);
      if (settingGoProfile == null) {
        break missingId;
      }

      id = R.id.settingGoResetPassword;
      ImageView settingGoResetPassword = ViewBindings.findChildViewById(rootView, id);
      if (settingGoResetPassword == null) {
        break missingId;
      }

      ConstraintLayout settingsContainer = (ConstraintLayout) rootView;

      id = R.id.settingsGoAboutUs;
      ImageView settingsGoAboutUs = ViewBindings.findChildViewById(rootView, id);
      if (settingsGoAboutUs == null) {
        break missingId;
      }

      id = R.id.settingsGoPrivacy;
      ImageView settingsGoPrivacy = ViewBindings.findChildViewById(rootView, id);
      if (settingsGoPrivacy == null) {
        break missingId;
      }

      id = R.id.switchMode;
      SwitchCompat switchMode = ViewBindings.findChildViewById(rootView, id);
      if (switchMode == null) {
        break missingId;
      }

      id = R.id.userImage;
      ImageView userImage = ViewBindings.findChildViewById(rootView, id);
      if (userImage == null) {
        break missingId;
      }

      id = R.id.username_settings;
      TextView usernameSettings = ViewBindings.findChildViewById(rootView, id);
      if (usernameSettings == null) {
        break missingId;
      }

      return new ActivitySettingsBinding((ConstraintLayout) rootView, PrivacyIcon, Settings,
          aboutUsIcon, bottomNavigation, changePasswordIcon, editProfile, nightModeIcon,
          notificationIcon, paymentIcon, scrollingSettings, settingGoProfile,
          settingGoResetPassword, settingsContainer, settingsGoAboutUs, settingsGoPrivacy,
          switchMode, userImage, usernameSettings);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
