// Generated by view binder compiler. Do not edit!
package com.teamten.visionfit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.teamten.visionfit.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final AppCompatButton btnRegister;

  @NonNull
  public final TextInputEditText email;

  @NonNull
  public final AppCompatImageView imageView;

  @NonNull
  public final TextView loginNow;

  @NonNull
  public final TextInputEditText password;

  @NonNull
  public final ProgressBar progressBar;

  private ActivityRegisterBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull AppCompatButton btnRegister, @NonNull TextInputEditText email,
      @NonNull AppCompatImageView imageView, @NonNull TextView loginNow,
      @NonNull TextInputEditText password, @NonNull ProgressBar progressBar) {
    this.rootView = rootView;
    this.btnRegister = btnRegister;
    this.email = email;
    this.imageView = imageView;
    this.loginNow = loginNow;
    this.password = password;
    this.progressBar = progressBar;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_register;
      AppCompatButton btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.email;
      TextInputEditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.imageView;
      AppCompatImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.loginNow;
      TextView loginNow = ViewBindings.findChildViewById(rootView, id);
      if (loginNow == null) {
        break missingId;
      }

      id = R.id.password;
      TextInputEditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((LinearLayoutCompat) rootView, btnRegister, email,
          imageView, loginNow, password, progressBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
