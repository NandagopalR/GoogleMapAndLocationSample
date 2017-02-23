package com.nanda.googlemapsample.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nanda.googlemapsample.R;
import com.nanda.googlemapsample.app.AppConstants;

public class HomeActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

  private GoogleApiClient mGoogleApiClient;
  private Location mLastLocation;
  private LocationRequest mLocationRequest;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    createGoogleApiClient();
  }

  private void createGoogleApiClient() {
    // Create an instance of GoogleAPIClient.
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }

    createLocationRequest();
  }

  @Override public void onConnected(@Nullable Bundle bundle) {

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (mLastLocation != null) {
      Toast.makeText(this, "Latitude - "
          + mLastLocation.getLatitude()
          + "\n Longitude - "
          + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override protected void onStop() {
    super.onStop();
    mGoogleApiClient.disconnect();
  }

  @Override protected void onPause() {
    super.onPause();
    stopLocationUpdates();
  }

  @Override protected void onResume() {
    super.onResume();
    if (mGoogleApiClient.isConnected()) startLocationUpdates();
  }

  protected void stopLocationUpdates() {
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
  }

  protected void startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    if (mGoogleApiClient != null) {
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
          this);
    }
  }

  protected void createLocationRequest() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(AppConstants.LOCATION_INTERVAL);
    mLocationRequest.setFastestInterval(AppConstants.LOCATION_FASTEST_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override public void onLocationChanged(Location location) {
    if (location != null) {
      Toast.makeText(this,
          "Latitude - " + location.getLatitude() + "\n Longitude - " + location.getLongitude(),
          Toast.LENGTH_SHORT).show();
    }
  }
}
