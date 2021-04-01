package com.example.cmput301w21t23_smartdatabook.geolocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class LocationWithPermission {
	private AppCompatActivity activity;
	private boolean success = false;

	public LocationWithPermission(AppCompatActivity activity) {
		this.activity = activity;
	}

	public boolean succeed() {
		return this.success;
	}

	private void getLatLng() {
        Dexter.withContext(activity.getApplicationContext())
			.withPermission(Manifest.permission.CAMERA)
			.withListener(new PermissionListener() {
				@Override
				public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
					// Get location, convert it to LatLng and return it
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
    }
}
