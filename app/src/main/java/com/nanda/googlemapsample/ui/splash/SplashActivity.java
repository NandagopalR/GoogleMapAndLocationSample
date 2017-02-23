package com.nanda.googlemapsample.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.nanda.googlemapsample.R;
import com.nanda.googlemapsample.app.AppConstants;
import com.nanda.googlemapsample.ui.home.HomeActivity;
import com.nanda.googlemapsample.utils.UiUtils;

/**
 * Created by nandagopal on 2/23/17.
 */
public class SplashActivity extends AppCompatActivity {

  private String mPermission[] = new String[] { Manifest.permission.ACCESS_COARSE_LOCATION };

  private Runnable homeRunnable = new Runnable() {
    @Override public void run() {
      navigateToHomeActivity();
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    requestPermission();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == AppConstants.REQUEST_PERMISSION_CODE) {
      boolean allPermissionGranted = true;
      if (grantResults.length == permissions.length) {
        for (int i = 0; i < permissions.length; i++) {
          if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            allPermissionGranted = false;
            break;
          }
        }
      } else {
        allPermissionGranted = false;
      }
      if (allPermissionGranted) {
        onPermissionGranted();
      } else {
        onPermissionDenied();
      }
    }
  }

  private void requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      boolean allPermissionsGranted = true;
      for (int i = 0, mPermissionLength = mPermission.length; i < mPermissionLength; i++) {
        String permission = mPermission[i];
        if (ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
          allPermissionsGranted = false;
          break;
        }
      }
      if (!allPermissionsGranted) {
        ActivityCompat.requestPermissions(this, mPermission, AppConstants.REQUEST_PERMISSION_CODE);
      } else {
        onPermissionGranted();
      }
    } else {
      onPermissionGranted();
    }
  }

  private void onPermissionGranted() {
    new Handler().postDelayed(homeRunnable, 1000);
  }

  private void onPermissionDenied() {
    UiUtils.showToast(this, "Permission Denied");
    finish();
  }

  private void navigateToHomeActivity() {
    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    finish();
  }
}
