package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.StringDate;
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

import java.util.HashMap;
import java.util.UUID;

/**
 * Class that builds a bit matrix which contains a message encoded in a QR code
 * returns the bitmap to be used in a image view
 * Refrences: https://github.com/kenglxn/QRGen,
 * https://stackoverflow.com/questions/8800919/how-to-generate-a-qr-code-for-an-android-application,
 * https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
 * @author Afaq Nabi
 */

public class QRCode {
    QRCodeWriter writer = new QRCodeWriter();

    Database database = Database.getDataBase();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StringDate stringDate = new StringDate();
    User user = User.getUser();

    /**
     * Generate function
     * @param content: a bitmap object showing the QR code?
     * @return
     */
    public Bitmap generate(String content){
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

    /**
     * This function is called if a QR code is scanned this function is called
     * use if-else statement to handle binomial QR codes, and other experiment type's QR codes
     * use for loop to create trial objects
     * @param rawResult: string consists the raw result
     */
    void QRCodeScanned(String rawResult){
        String[] values = rawResult.split(",");

        if (values[3].equals("Binomial")){
            //Need to add in given number of binomial trials
            for (int i = 1; i <= Integer.parseInt(values[2]); i++ ){
                Trial trial = new Trial( Boolean.parseBoolean(values[4]),
                        values[3],
                        Boolean.parseBoolean(values[5]),
                        values[1],
                        UUID.randomUUID().toString(),
                        stringDate.getCurrentDate());
                database.addTrialToDB(db.collection("Experiments")
                        .document(values[0])
                        .collection("Trials")
                        .document(trial.getTrialID()), trial);
            }

        } else{
            Trial trial = new Trial( Boolean.parseBoolean(values[4]),
                    values[3],
                    Float.parseFloat(values[2]),
                    values[1],
                    UUID.randomUUID().toString(),
                    stringDate.getCurrentDate());
            database.addTrialToDB(db.collection("Experiments")
                    .document(values[0])
                    .collection("Trials")
                    .document(trial.getTrialID()), trial);
        }
    }

    /**
     * This function is called if barcode is scanned of the purpose of adding a trial to the experiment
     * It finds the experiment that user wish to add trial on
     * Once the barcode is properly scanned, it will add trial objects through barcode
     * @param rawResult: a string object consists the raw result
     * @param experiment: an experiment object consists the experiment itself
     */
    void BarcodeScanned(String rawResult, Experiment experiment){
        db.collection("Barcode")
                .document(experiment.getExpID())
                .collection(user.getUserUniqueID())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (rawResult.equals(document.get("RawResult"))){
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
                    }
                    // TODO: HANDLE BARCODE NOT EXISTS
                    // TODO: have not handled binomial barcode trial
                }
            }
        });
    }



}
