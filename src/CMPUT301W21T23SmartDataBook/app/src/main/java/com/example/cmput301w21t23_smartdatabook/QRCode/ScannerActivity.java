package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.StringDate;
import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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


/**
 * this class will use the emulators camera to scan the barcode using the Zxing librabry
 * once scanned the rawResult will be added to the database
 * references: https://www.youtube.com/watch?v=AiNi9K94W5c&ab_channel=MdJamal
 * @author Afaq Nabi
 * @see QRCode
 */

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
	ZXingScannerView scannerView;
	Database database = Database.getDataBase();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	StringDate stringDate = new StringDate();
	Experiment experiment;
	User user = User.getUser();

	/**
	 * This functuon sets up the view when the camera us scanning the barcode
	 * It deals with the user chooses to accept/ deny permission for the app to use the camera
	 * If the app can't use the phone's camera because the user denied permission, then display a dialog to notify the user
	 * @param savedInstanceState
	 */
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
								}
						).show();

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

	/**
	 * This functions handles the captured result from the user's phone camera
	 * we use serliazable to pass in experiment data, we also use if statmenet to check whether the captured results has a QR code
	 * @param rawResult
	 */
	@Override
	public void handleResult(Result rawResult) {
		Log.e("format", rawResult.getBarcodeFormat().toString());
		Intent intent = getIntent();
		experiment = (Experiment) intent.getSerializableExtra("experiment");
		String type = intent.getStringExtra("Flag");
		if (type.equals("Scan")){
			if (rawResult.getBarcodeFormat().toString().contains("QR_CODE")) {

				QRcode.QRCodeScanned(rawResult.toString());

			} else {
				QRcode.BarcodeScanned(rawResult.toString(),experiment);
			}
			onBackPressed();
		}
		else {
			registerBarcode(rawResult.toString(), experiment);
		}

	}

	/**
	 * onPause function, it stops the camera
	 */
	@Override
	protected void onPause() {
		super.onPause();
		scannerView.stopCamera();
	}

	/**
	 * onResume function, resume the view
	 */
	@Override
	protected void onResume() {
		super.onResume();
		scannerView.setResultHandler(this);
	}

	/**
	 * onDestroy function, it stops the camera as well
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		scannerView.stopCamera();
	}

	/**
	 * This function runs if a barcode or anything other than a QR code is scanned for the purpose of registering it to a trial for an experiment
	 * @param rawResult: string object showing the raw result
	 * @param experiment: Experiment object represents an experiment
	 */
	private void registerBarcode(String rawResult, Experiment experiment){
		View trialView = LayoutInflater.from(ScannerActivity.this).inflate(R.layout.barcode, null);
		EditText value = trialView.findViewById(R.id.value);
		TextView trueFalse = trialView.findViewById(R.id.true_false);
		Switch switchTF = trialView.findViewById(R.id.switchTF);

		// use if-else statement to handle input type and increment input type
		// if the trial type is binomial then true false switch will be set to visible
		int inputType = InputType.TYPE_CLASS_NUMBER;
		if (experiment.getTrialType().equals("Count")) {
			inputType += InputType.TYPE_NUMBER_FLAG_SIGNED;
		} else if (experiment.getTrialType().equals("Measurement")) {
			inputType += InputType.TYPE_NUMBER_FLAG_DECIMAL;
		} else if (experiment.getTrialType().equals("Binomial")) {
			trueFalse.setVisibility(View.VISIBLE);
			switchTF.setVisibility(View.VISIBLE);
		}
		value.setInputType(inputType);

		// displaying the switch
		switchTF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (switchTF.isChecked()) {
					trueFalse.setText("True");
				} else {
					trueFalse.setText("False");
				}
			}
		});

		// use a dialog to show enter value for trial
		AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
		builder.setTitle("Enter value for trial")
				.setView(trialView)
				.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								if (rawResult.equals(document.get("RawResult"))) {
									onBackPressed();
									Toast.makeText(getBaseContext(), "Barcode already exists for this experiment", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
							builder.setTitle("Enter value for trial")
									.setView(trialView)
									.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											onBackPressed();
										}
									})
									.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											HashMap<String, Object> data = new HashMap<>();
											data.put("RawResult", rawResult);
											data.put("UUID", user.getUserUniqueID());
											data.put("ExpID", experiment.getExpID());
											if (experiment.getTrialType().equals("Binomial")) {
												data.put("Bool", switchTF.isChecked());
												data.put("Value", Integer.parseInt(value.getText().toString()));
											} else if (experiment.getTrialType().equals(("Measurement"))) {
												data.put("Value", Float.parseFloat(value.getText().toString()));
											} else {
												data.put("Value", Integer.parseInt(value.getText().toString()));
											}
											Log.e("value", String.valueOf(value.getText()));

											db.collection("Barcode")
													.document(experiment.getExpID())
													.collection(user.getUserUniqueID())
													.document(UUID.randomUUID().toString())
													.set(data);
											onBackPressed();
										}
									})
									.create()
									.show();
						}
					}
				});

	}

	// if a QR code is scanned this function is called
	private void QRCodeScanned(String rawResult) {
		new LocationWithPermission(ScannerActivity.this).getLatLng(new GeneralDataCallBack() {
			@Override
			public void onDataReturn(Object returnedObject) {
				Location location = (Location) returnedObject;
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				String[] values = rawResult.split(",");
				if (experiment.getExpID().equals(values[0])){
					if (values[3].equals("Binomial")) {
						db
								.collection("Experiments")
								.document(values[0])
								.collection("Trials")
								.get()
								.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
									@Override
									public void onComplete(@NonNull Task<QuerySnapshot> task) {
										if (task.isSuccessful()) {
											int count = 0;
											for (QueryDocumentSnapshot document : task.getResult()) {
												count += 1;
											}
											if ((count + Integer.parseInt(values[2])) >= experiment.getMaxTrials()) {
												Toast.makeText(ScannerActivity.this, "You cannot add more trials than the maximum trials for this experiment at once", Toast.LENGTH_SHORT).show();
											} else {
												for (int i = 0; i < Integer.parseInt(values[2]); i++) {
													Trial trial = new Trial(Boolean.parseBoolean(values[4]),
															values[3],
															Boolean.parseBoolean(values[5]),
															values[1],
															UUID.randomUUID().toString(),
															stringDate.getCurrentDate(),
															experiment.getRequireLocation() ? latlng : null);
													database.addTrialToDB(db.collection("Experiments")
															.document(values[0])
															.collection("Trials")
															.document(trial.getTrialID()), trial);
												}
											}
										}
									}
								});
					} else {
						db
								.collection("Experiments")
								.document(values[0])
								.collection("Trials")
								.get()
								.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
									@Override
									public void onComplete(@NonNull Task<QuerySnapshot> task) {
										if (task.isSuccessful()) {
											int count = 0;
											for (QueryDocumentSnapshot document : task.getResult()) {
												count += 1;
											}
											if (count< experiment.getMaxTrials()){
												Trial trial = new Trial(Boolean.parseBoolean(values[4]),
														values[3],
														Float.parseFloat(values[2]),
														values[1],
														UUID.randomUUID().toString(),
														stringDate.getCurrentDate(),
														experiment.getRequireLocation() ? latlng : null);

												database.addTrialToDB(db.collection("Experiments")
														.document(values[0])
														.collection("Trials")
														.document(trial.getTrialID()), trial);
											}
										}
									}
								});
					}

				} else{
					onBackPressed();
					Toast.makeText(getBaseContext(), "That QR code is not valid for this experiment", Toast.LENGTH_SHORT).show();
				}


			}
		});
	}

	// if barcode is scanned ofr hte purpose of adding a trila to the experiment
	private void BarcodeScanned(String rawResult, Experiment experiment) {
		new LocationWithPermission(ScannerActivity.this).getLatLng(new GeneralDataCallBack() {
			@Override
			public void onDataReturn(Object returnedObject) {
				Location location = (Location) returnedObject;
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				db.collection("Barcode")
						.document(experiment.getExpID())
						.collection(user.getUserUniqueID())
						.get()
						.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HashMap<String, Object> data = new HashMap<>();
						data.put("RawResult", rawResult);
						data.put("UUID", user.getUserUniqueID());
						data.put("ExpID", experiment.getExpID());
						if (experiment.getTrialType().equals("Binomial")) {
							data.put("Bool", switchTF.isChecked());
							data.put("Value", Integer.parseInt(value.getText().toString()));
						} else if (experiment.getTrialType().equals(("Measurement"))) {
							data.put("Value", Float.parseFloat(value.getText().toString()));
						} else {
							data.put("Value", Integer.parseInt(value.getText().toString()));
						}
						Log.e("value", String.valueOf(value.getText()));

						db.collection("Barcode")
								.document(experiment.getExpID())
								.collection(user.getUserUniqueID())
								.document(UUID.randomUUID().toString())
								.set(data);
						onBackPressed();
					}
				})
				.create()
				.show();
	}

}

