package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.mainController.MainActivity;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
	ZXingScannerView scannerView;
	Database database = new Database();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scannerView=new ZXingScannerView(this);
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

					}

					@Override
					public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
						permissionToken.continuePermissionRequest();
					}
				}).check();
	}

	@Override
	public void handleResult(Result rawResult) {
		String data=rawResult.getText().toString();
		Log.d("Test", data);

//		dbref.push().setValue(data)
//				.addOnCompleteListener(new OnCompleteListener<Void>() {
//					@Override
//					public void onComplete(@NonNull Task<Void> task) {
//						MainActivity.qrtext.setText("Data inserted Successfully");
//						onBackPressed();
//					}
//				});
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
//		scannerView.startCamera();
	}
}

