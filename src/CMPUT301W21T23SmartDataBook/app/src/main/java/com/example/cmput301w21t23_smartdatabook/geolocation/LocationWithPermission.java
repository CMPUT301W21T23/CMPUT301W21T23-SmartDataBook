package com.example.cmput301w21t23_smartdatabook.geolocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jayden
 */
public class LocationWithPermission {
	private AppCompatActivity activity;
	private boolean success = false;

	public LocationWithPermission(AppCompatActivity activity) {
		this.activity = activity;
	}

	public FusedLocationProviderClient getLatLng(LocationCallback m) {
		FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        Dexter.withContext(activity.getApplicationContext())
			.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
			.withListener(new PermissionListener() {
				@Override
				public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
			        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			            // Following check removed from conditionals
			        	// ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
	                        	Log.d("lwpInstance", "working?");
				         fusedLocationClient.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), m, Looper.myLooper());

				        // Below method executes when location is successfully obtained, deprecated due to above mLocationCallback
//			            fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
//			                @Override
//			                public void onSuccess(Location location) {
//			                    // Got last known location. In some rare situations this can be null.
//				                LocationResult temp = new LocationResult(new List<Location>() );
//			                    m.onLocationResult();
//			                }
//			            });
			        }
				}
				@Override
				public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Toast.makeText(activity.getBaseContext(), "" + permissionDeniedResponse.isPermanentlyDenied(), Toast.LENGTH_SHORT).show();

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
//        generalDataCallBack.onDataReturn(null);
		return fusedLocationClient;
    }
}
