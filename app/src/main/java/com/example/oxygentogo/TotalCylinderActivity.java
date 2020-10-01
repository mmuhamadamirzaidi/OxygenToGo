package com.example.oxygentogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.example.oxygentogo.Util.VolleyHelper;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import mehdi.sakout.fancybuttons.FancyButton;

public class TotalCylinderActivity extends BaseActivity implements
        AdapterView.OnItemSelectedListener,
        DiscreteSeekBar.OnProgressChangeListener,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        Response.ErrorListener,
        Response.Listener<JSONObject> {

   @BindView(R.id.button_calculate)
   FancyButton buttonCalculate;

   @BindView(R.id.button_location)
   FancyButton buttonLocation;

   @BindView(R.id.spinner_cylinder_type)
   Spinner spinnerCylinder;

   @BindView(R.id.spinner_mask_type)
   Spinner spinnerMask;

   @BindView(R.id.seekBar)
   DiscreteSeekBar seekBar;

   @BindView(R.id.textView_indicator)
   TextView textViewIndicator;

   @BindView(R.id.toolbar)
   Toolbar toolbar;

   AlertDialog ad, adResult;
   GoogleApiClient googleApiClient;
   FancyButton buttonStart, buttonEnd, buttonProceed;
   ImageView buttonX;
   Place placeStart, placeEnd;
   int timeTaken = 0, flowRate = 1;
   List<String> listCylinder;
   TextView textViewHint;
   String strLoc;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_total_cylinder);

      toolbar.setTitle("Jumlah Silinder");
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      listCylinder = new ArrayList<>();
      listCylinder.add("Silinder D");
      listCylinder.add("Silinder E");
      listCylinder.add("Silinder F");

      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCylinder);
      spinnerCylinder.setAdapter(adapter);

      List<String> listMask = new ArrayList<>();
      listMask.add("Nasal Prong: 1-5L");
      listMask.add("Face Mask: 5-8L");
      listMask.add("Highflow Mask: 8-15L");
      listMask.add("Ventilator: 25L");

      ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMask);
      spinnerMask.setAdapter(adapter2);

      spinnerMask.setOnItemSelectedListener(this);
      seekBar.setOnProgressChangeListener(this);

      buttonLocation.setOnClickListener(this);
      buttonCalculate.setOnClickListener(this);
   }

   public void getBothLocation() {
      AlertDialog.Builder adb = new AlertDialog.Builder(this);

      View v = getLayoutInflater().inflate(R.layout.adb_choose_location, null);
      adb.setView(v);

      buttonProceed = (FancyButton) v.findViewById(R.id.button_proceed);
      buttonStart = (FancyButton) v.findViewById(R.id.button_start);
      buttonEnd = (FancyButton) v.findViewById(R.id.button_end);
      buttonX = (ImageView) v.findViewById(R.id.button_x);
      textViewHint = (TextView) v.findViewById(R.id.textView_hint);

      buttonProceed.setOnClickListener(this);
      buttonEnd.setOnClickListener(this);
      buttonStart.setOnClickListener(this);
      buttonX.setOnClickListener(this);

      ad = adb.create();
      ad.show();
   }

   @Override
   public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
      switch (adapterView.getId()) {
         case R.id.spinner_mask_type:
            if (position == 0) {
               textViewIndicator.setText("1L/min");
               seekBar.setProgress(0);
               seekBar.setMin(1);
               seekBar.setMax(5);
               seekBar.setProgress(1);
            } else if (position == 1) {
               textViewIndicator.setText("5L/min");
               flowRate = 5;
               seekBar.setProgress(0);
               seekBar.setMin(5);
               seekBar.setMax(8);
               seekBar.setProgress(5);
            } else if (position == 2) {
               textViewIndicator.setText("8L/min");
               flowRate = 8;
               seekBar.setProgress(0);
               seekBar.setMin(8);
               seekBar.setMax(15);
               seekBar.setProgress(8);
            } else if (position == 3) {
               textViewIndicator.setVisibility(View.GONE);
               seekBar.setVisibility(View.GONE);
               flowRate = 25;
               textViewIndicator.setText("25L/min");
            }
            break;
      }
   }

   @Override
   public void onNothingSelected(AdapterView<?> adapterView) {

   }

   @Override
   public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
      textViewIndicator.setText(String.format(Locale.getDefault(), "%dL/min", value));
      flowRate = value;
   }

   @Override
   public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

   }

   @Override
   public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

   }

   @Override
   public void onClick(View view) {

      switch (view.getId()) {
         case R.id.button_proceed:
            ad.dismiss();
            String text = "Dari " + buttonStart.getText().toString() + "\nke " + buttonEnd.getText().toString() + strLoc;
            buttonLocation.setText(text);
            break;
         case R.id.button_location:
            getBothLocation();
            break;

         case R.id.button_start:
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            try {
               startActivityForResult(intentBuilder.build(TotalCylinderActivity.this), 1);
            } catch (GooglePlayServicesRepairableException e) {
               Toast.makeText(this, "Google Play Services Repairable Exception", Toast.LENGTH_SHORT).show();
            } catch (GooglePlayServicesNotAvailableException e) {
               Toast.makeText(this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }

            break;

         case R.id.button_end:
            PlacePicker.IntentBuilder intentBuilder2 = new PlacePicker.IntentBuilder();
            try {
               startActivityForResult(intentBuilder2.build(TotalCylinderActivity.this), 2);
            } catch (GooglePlayServicesRepairableException e) {
               Toast.makeText(this, "Google Play Services Repairable", Toast.LENGTH_SHORT).show();
            } catch (GooglePlayServicesNotAvailableException e) {
               Toast.makeText(this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
            break;

         case R.id.button_calculate:
            showResult();
            break;
         case R.id.button_close:
            adResult.dismiss();
            break;
         case R.id.button_x:
            ad.dismiss();
            break;
      }
   }

   private void getMatrixApi() {
      double p1Lat = placeStart.getLatLng().latitude;
      double p1Lon = placeStart.getLatLng().longitude;

      double p2Lat = placeEnd.getLatLng().latitude;
      double p2Lon = placeEnd.getLatLng().longitude;
      String url = "https://maps.googleapis.com/maps/api/distancematrix";
      VolleyHelper volleyHelper = new VolleyHelper(this, url);

      volleyHelper.get("json?origins=" + p1Lat + "," + p1Lon + "&destinations=" + p2Lat + "," + p2Lon + "&key=AIzaSyCavddnrHBb26R8M0CZERZ2jHD5Ryytzas", null, this, this);
   }

   public void showResult() {
      AlertDialog.Builder adb = new AlertDialog.Builder(this);

      double volumeNeeded = timeTaken * flowRate * 2000 / 2000;
      Log.e("Time Taken", timeTaken + "");
      Log.e("Flow Rate", flowRate + "");
      Log.e("VolumeNeeded", volumeNeeded + "");
      double cylinderNeeded = volumeNeeded / getVolumeForCylinder(spinnerCylinder.getSelectedItemPosition());
      int floor = (int) Math.ceil(cylinderNeeded);
      // Log.e("Vol4Cylinder", String.valueOf(getVolumeForCylinder(spinnerCylinder.getSelectedItemPosition())));
      // Log.e("Cylinder Needed", String.valueOf(cylinderNeeded));

      View view = getLayoutInflater().inflate(R.layout.adb_total_cylinder_result, null);
      adb.setView(view);

      adResult = adb.create();
      adResult.show();

      FancyButton buttonClose = (FancyButton) view.findViewById(R.id.button_close);
      TextView textViewLabel = (TextView) view.findViewById(R.id.textView_label);

      String strLabel = floor + " silinder";
      textViewLabel.setText(strLabel);

      buttonClose.setOnClickListener(this);
   }

   @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

   }

   @Override
   public void onConnected(@Nullable Bundle bundle) {

   }

   @Override
   public void onConnectionSuspended(int i) {

   }

   @Override
   public void onStart() {
      super.onStart();
      if (googleApiClient != null) {
         googleApiClient.connect();
      }
   }

   @Override
   public void onStop() {
      if (googleApiClient != null && googleApiClient.isConnected()) {
         googleApiClient.disconnect();
      }
      super.onStop();
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
         displayPlace(PlacePicker.getPlace(this, data), requestCode);
      }

      if (requestCode == 2 && resultCode == RESULT_OK) {
         buttonProceed.setVisibility(View.VISIBLE);
         textViewHint.setVisibility(View.VISIBLE);
         getMatrixApi();
      }
   }

   private void displayPlace(Place place, int requestCode) {
      if (place == null) {
         return;
      }

      if (requestCode == 1) {
         placeStart = place;
         buttonStart.setText(place.getName().toString());
      } else if (requestCode == 2) {
         placeEnd = place;
         buttonEnd.setText(place.getName().toString());
      }
   }

   @Override
   public void onErrorResponse(VolleyError error) {

   }

   @Override
   public void onResponse(JSONObject response) {
      Log.e("Response", response.toString());
      try {
         JSONArray rows = response.getJSONArray("rows");
         JSONObject row = rows.getJSONObject(0);

         JSONArray elements = row.getJSONArray("elements");
         JSONObject element = elements.getJSONObject(0);

         JSONObject distance = element.getJSONObject("distance");
         JSONObject duration = element.getJSONObject("duration");

         String strDistance = distance.getString("text");
         String strDuration = duration.getString("text");

         strLoc = "\n\n" + strDistance + " @ " + strDuration;

         String strMin = strDistance.replaceAll("[^\\d]", "");
         timeTaken = Integer.parseInt(strMin);

         buttonLocation.setText(strLoc);
         textViewHint.setVisibility(View.GONE);
      } catch (JSONException e) {
         e.printStackTrace();
      }

   }

   public int getVolumeForCylinder(int position) {
      int value = 0;
      if (position == 0) {
         value = 500;
      } else if (position == 1) {
         value = 700;
      } else if (position == 2) {
         value = 1400;
      }
      return value;
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
      switch (requestCode) {
         case 1:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               initializeAndConnectGoogleApi();
            } else {
               new AlertDialog.Builder(this)
                       .setMessage("Sila benarkan penggunaan GPS untuk menggunakan fungsi ini")
                       .setTitle("Kebenaran Terhalang")
                       .setCancelable(false)
                       .setPositiveButton("Cuba Lagi", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                             ActivityCompat.requestPermissions(TotalCylinderActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                          }
                       })
                       .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                             finish();
                          }
                       })
                       .show();
            }
            break;

      }
   }

   private void showGPSDisabledAlertToUser() {
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
      alertDialogBuilder.setMessage("GPS tidak diaktifkan. Sila aktifkan dahulu")
              .setCancelable(false)
              .setPositiveButton("Go to Settings",
                      new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                         }
                      });
      alertDialogBuilder.setNegativeButton("Cancel",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                 }
              });
      AlertDialog alert = alertDialogBuilder.create();
      alert.show();
   }

   public void onResume() {
      super.onResume();
      LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
      if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeAndConnectGoogleApi();
         } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
         }
      } else {
         Toast.makeText(this, "GPS tidak diaktifkan", Toast.LENGTH_SHORT).show();
         showGPSDisabledAlertToUser();
      }

   }

   // Stop listening to avoid battery consumption and leak
   @Override
   protected void onPause() {
      super.onPause();
      if (googleApiClient != null) {
         googleApiClient.stopAutoManage(this);
         googleApiClient.disconnect();
      }
   }

   private void initializeAndConnectGoogleApi() {
      // Only create a new instance when the client is null
      if (googleApiClient != null) {
         googleApiClient.stopAutoManage(this);
         googleApiClient.disconnect();
      }
      googleApiClient = new GoogleApiClient
              .Builder(this)
              .enableAutoManage(this, 0, this)
              .addApi(Places.GEO_DATA_API)
              .addApi(Places.PLACE_DETECTION_API)
              .addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
              .build();
      // Connect if it's not
      if (!googleApiClient.isConnected()) {
         googleApiClient.connect();
      }
   }
}
