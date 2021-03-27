package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
// https://github.com/kenglxn/QRGen
// https://stackoverflow.com/questions/8800919/how-to-generate-a-qr-code-for-an-android-application
// https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
public class QRCode {
    QRCodeWriter writer = new QRCodeWriter();

    public Bitmap generate(String content){
        BitMatrix bitMatrix = null;

        try {
            bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
        } catch (WriterException writerException) {
            writerException.printStackTrace();
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
//        ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
        return bmp;
    }

    public void qrCodeScanner(){
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC,
                                Barcode.FORMAT_CODABAR)
                        .build();
        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);
    }

//    try {
//            String content= "m";
//            BitMatrix bitMatrix = null;
//            try {
//                bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
//            } catch (WriterException writerException) {
//                writerException.printStackTrace();
//            }
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
////        ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
//
//        } catch(WriterException e) {
//            e.printStackTrace();
//        }


}
