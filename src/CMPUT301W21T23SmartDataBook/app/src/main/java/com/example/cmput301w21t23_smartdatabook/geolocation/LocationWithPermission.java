package com.example.cmput301w21t23_smartdatabook.geolocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Class: LocationWithPermission
 * This class is used to obtain the location, while it also handles the problems with permissions.
 *
 * @author Jayden Cho
 */
public class LocationWithPermission {
	private final AppCompatActivity activity;

	public LocationWithPermission(AppCompatActivity activity) {
		this.activity = activity;
	}

	/**
	 * This initiates a request for location update, while handling permissions as well.
	 */
	public void requestLocationUpdate() {
		Dexter.withContext(activity.getApplicationContext())
				.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
						if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
							// Following check removed from conditionals
							// ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
							LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(LocationRequest.create().setInterval(15000).setFastestInterval(1000).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), new LocationCallback() {
							}, Looper.myLooper());

							// Below method executes when location is successfully obtained, deprecated due to above mLocationCallback
//							fusedLocationClient.getLastLocation().addOnSuccessListener(activity, generalDataCallBack::onDataReturn);
						}
					}

					@Override
					public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
						// Reused code from scanner activity
						//Source: Opeyemi, https://stackoverflow.com/users/8226150/opeyemi
						//Code: https://stackoverflow.com/questions/50639292/detecting-wether-a-permission-can-be-requested-or-is-permanently-denied
						if (permissionDeniedResponse.isPermanentlyDenied()) {
							//permission is permanently denied navigate to user setting
							new AlertDialog.Builder(activity)
									.setTitle("Camera permission was denied permanently.")
									.setMessage("Allow Camera access through your settings.")
									.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
											Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
											intent.setData(uri);
											activity.startActivityForResult(intent, 101);
											dialog.cancel();
										}
									})
									.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											}
									).show();
						}
					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
						permissionToken.continuePermissionRequest();

					}
				}).check();
	}
}
