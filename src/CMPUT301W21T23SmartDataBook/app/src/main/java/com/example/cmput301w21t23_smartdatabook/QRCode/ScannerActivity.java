package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
	ZXingScannerView scannerView;
	Database database = Database.getDataBase();
	FirebaseFirestore db = FirebaseFirestore.getInstance();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scannerView = new ZXingScannerView(this);
		setContentView(scannerView);

		Dexter.withContext(getApplicationContext())
				.withPermission(Manifest.permission.CAMERA)
				.withListener(new PermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
						scannerView.startCamera();
					}
					@Override
					public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
						Toast.makeText(getBaseContext(), ""+permissionDeniedResponse.isPermanentlyDenied(),  Toast.LENGTH_SHORT).show();

						//Source: Opeyemi, https://stackoverflow.com/users/8226150/opeyemi
						//Code: https://stackoverflow.com/questions/50639292/detecting-wether-a-permission-can-be-requested-or-is-permanently-denied
						if (permissionDeniedResponse.isPermanentlyDenied()){
							//permission is permanently denied navigate to user setting
							new AlertDialog.Builder(ScannerActivity.this)
									.setTitle("Camera permission was denied permanently.")
									.setMessage("Allow Camera access through your settings.")
									.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
											Uri uri = Uri.fromParts("package", getPackageName(), null);
											intent.setData(uri);
											startActivityForResult(intent, 101);
											dialog.cancel();
											finish();
										}
									})
									.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.cancel();
											finish();
										}
									}).show();

						} else {
							finish();
						}

					}
					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
						permissionToken.continuePermissionRequest();

					}
				}).check();

	}

	@Override
	public void handleResult(Result rawResult) {

		HashMap<String, String> data = new HashMap<>();

		String[] values = rawResult.getText().split(",");
		String trialUUID = UUID.randomUUID().toString();

//		data.put("Region On", values[4]);
//		data.put("Trial Type", values[3]);
//		data.put("Trial Value", values[2]);
//		data.put("TrialID", ""+trialUUID);
//		data.put("UUID", values[1]);

		if (values[3].equals("Binomial")){
			//Need to add in given number of binomial trials
			for (int i = 1; i <= Integer.parseInt(values[2]); i++ ){
				Trial trial = new Trial( Boolean.parseBoolean(values[4]),
						values[3],
						true,
						values[1],
						UUID.randomUUID().toString());
				database.addTrialToDB(db.collection("Experiments")
						.document(values[0])
						.collection("Trials")
						.document(trial.getTrialID()), trial);
			}
			onBackPressed();

		}else{

			Trial trial = new Trial( Boolean.parseBoolean(values[4]),
					values[3],
					true,
					values[1],
					UUID.randomUUID().toString());
			database.addTrialToDB(db.collection("Experiments")
					.document(values[0])
					.collection("Trials")
					.document(trial.getTrialID()), trial);

//			FirebaseFirestore.getInstance().collection("Experiments").document(values[0]).collection("Trials").document(""+trialUUID)
//					.set(data)
//					.addOnCompleteListener(new OnCompleteListener<Void>() {
//						@Override
//						public void onComplete(@NonNull Task<Void> task) {
//							Toast.makeText(getBaseContext(), "Successfully saved QR value",  Toast.LENGTH_SHORT).show();
//							onBackPressed();
//						}
//					});
		}


	}

	@Override
	protected void onPause() {
		super.onPause();
		scannerView.stopCamera();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scannerView.setResultHandler(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		scannerView.stopCamera();
	}

}

