// Generated by view binder compiler. Do not edit!
package com.teamten.visionfit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.teamten.visionfit.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final CheckBox checkBoxRememberMe;

  @NonNull
  public final TextInputEditText email;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextInputEditText password;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final TextView registerNow;

  @NonNull
  public final TextView txtForgotPassword;

  private ActivityLoginBinding(@NonNull LinearLayout rootView, @NonNull Button btnLogin,
      @NonNull CheckBox checkBoxRememberMe, @NonNull TextInputEditText email,
      @NonNull ImageView imageView, @NonNull TextInputEditText password,
      @NonNull ProgressBar progressBar, @NonNull TextView registerNow,
      @NonNull TextView txtForgotPassword) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.checkBoxRememberMe = checkBoxRememberMe;
    this.email = email;
    this.imageView = imageView;
    this.password = password;
    this.progressBar = progressBar;
    this.registerNow = registerNow;
    this.txtForgotPassword = txtForgotPassword;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_login;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.checkBoxRememberMe;
      CheckBox checkBoxRememberMe = ViewBindings.findChildViewById(rootView, id);
      if (checkBoxRememberMe == null) {
        break missingId;
      }

      id = R.id.email;
      TextInputEditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
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

      id = R.id.registerNow;
      TextView registerNow = ViewBindings.findChildViewById(rootView, id);
      if (registerNow == null) {
        break missingId;
      }

      id = R.id.txtForgotPassword;
      TextView txtForgotPassword = ViewBindings.findChildViewById(rootView, id);
      if (txtForgotPassword == null) {
        break missingId;
      }

      return new ActivityLoginBinding((LinearLayout) rootView, btnLogin, checkBoxRememberMe, email,
          imageView, password, progressBar, registerNow, txtForgotPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
