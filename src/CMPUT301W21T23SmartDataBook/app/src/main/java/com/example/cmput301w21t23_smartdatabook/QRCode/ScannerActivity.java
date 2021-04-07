package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
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
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.geolocation.LocationWithPermission;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.trials.UploadTrial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.maps.model.LatLng;
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
 * @author Afaq
 */
public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
	ZXingScannerView scannerView;
	Database database = Database.getDataBase();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	StringDate stringDate = new StringDate();
	Experiment experiment;
	User user = User.getUser();
	QRCode QRcode = new QRCode();

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

	@Override
	public void handleResult(Result rawResult) {
		Log.e("format", rawResult.getBarcodeFormat().toString());
		Intent intent = getIntent();
		experiment = (Experiment) intent.getSerializableExtra("experiment");
		String type = intent.getStringExtra("Flag");
		if (type.equals("Scan")){
			if (rawResult.getBarcodeFormat().toString().contains("QR_CODE")) {
				QRCodeScanned(rawResult.toString());

			} else {
				BarcodeScanned(rawResult.toString(),experiment);
			}
			onBackPressed();
		}
		else {
			registerBarcode(rawResult.toString(), experiment);
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

	// if a barcode or anything other than a QR code is scanned for th epurpose of
	// registering it to a trial for an experiment
	private void registerBarcode(String rawResult, Experiment experiment){
		View trialView = LayoutInflater.from(ScannerActivity.this).inflate(R.layout.barcode, null);
		EditText value = trialView.findViewById(R.id.value);
		TextView trueFalse = trialView.findViewById(R.id.true_false);
		Switch switchTF = trialView.findViewById(R.id.switchTF);

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

		db.collection("Barcode")
				.document(experiment.getExpID())
				.collection(user.getUserUniqueID())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

				if (values[3].equals("Binomial")) {
					//Need to add in given number of binomial trials
					for (int i = 1; i <= Integer.parseInt(values[2]); i++) {
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

				} else {
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
		});
	}

	// if barcode is scanned for the purpose of adding a trial to the experiment
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
							public void onComplete(@NonNull Task<QuerySnapshot> task) {
								if (task.isSuccessful()) {
									for (QueryDocumentSnapshot document : task.getResult()) {
										if (rawResult.equals(document.get("RawResult"))) {
											if (experiment.getTrialType().equals("Binomial")){
												for (int i = 1; i <= Integer.parseInt((String) document.get("Value")); i++) {
													Trial trial = new Trial(experiment.getRequireLocation(),
															experiment.getTrialType(),
															document.get("Bool"),
															user.getUserUniqueID(),
															UUID.randomUUID().toString(),
															stringDate.getCurrentDate(),
															experiment.getRequireLocation() ? latlng : null);

													database.addTrialToDB(db
															.collection("Experiments")
															.document(experiment.getExpID())
															.collection("Trials")
															.document(trial.getTrialID()), trial);
												}
											}
											else{
												Trial trial = new Trial(experiment.getRequireLocation(),
														experiment.getTrialType(),
														document.get("Value"),
														user.getUserUniqueID(),
														UUID.randomUUID().toString(),
														stringDate.getCurrentDate());

												database.addTrialToDB(db
														.collection("Experiments")
														.document(experiment.getExpID())
														.collection("Trials")
														.document(trial.getTrialID()), trial);
											}
											return; // HANDLE BARCODE NOT EXISTS
										}
									}
								}
							}
						});
			}
		});
	}
}

