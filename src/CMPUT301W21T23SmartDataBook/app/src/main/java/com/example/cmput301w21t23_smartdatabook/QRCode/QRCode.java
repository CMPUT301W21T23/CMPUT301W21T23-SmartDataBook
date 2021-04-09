package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.stats.StringDate;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.UUID;

/**
 * Class that builds a bit matrix which contains a message encoded in a QR code
 * returns the bitmap to be used in a image view
 * Refrences: https://github.com/kenglxn/QRGen,
 * https://stackoverflow.com/questions/8800919/how-to-generate-a-qr-code-for-an-android-application,
 * https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
 *
 * @author Afaq Nabi
 */

public class QRCode {
	QRCodeWriter writer = new QRCodeWriter();

	/**
	 * Generate function
	 *
	 * @param content: a bitmap object showing the QR code?
	 * @return
	 */
	public Bitmap generate(String content) {
		BitMatrix bitMatrix = null;

		// use try-catch block to encode a bit matrix
		try {
			bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
		} catch (WriterException writerException) {
			writerException.printStackTrace();
		}

		// set up bitmatrix
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

}
