package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.button.MaterialButton;

/**
 * generate QR code activity
 * this will call the QR code based on input and show it to the user
 * its the user's responsibility to save the  QR code
 * @author Afaq Nabi, Bosco Chan
 * @see QRCode
 */
public class QRCodeActivity extends AppCompatActivity {
    User user = User.getUser();
    QRCode QRcode = new QRCode();
    private static final int passID = 1;
    private static final int failID = 0;

    /**
     * onCreate method that sets up the screen of generating QR code
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_qr);

        setSupportActionBar(findViewById(R.id.app_toolbar));
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

        toolbar.setTitle(experiment.getExpName());

        Button regBarcode = findViewById(R.id.barcode);
        regBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeActivity.this, ScannerActivity.class);
                intent.putExtra("experiment", (Parcelable) experiment);
                intent.putExtra("Flag", "Register");
                startActivity(intent);
            }
        });



        EditText value = findViewById(R.id.passesEditTextQR);

        RadioGroup binoChoice = findViewById(R.id.binoRadioGroup);
        RadioButton pass = findViewById(R.id.pass_radio_button);
        RadioButton fail = findViewById(R.id.fail_radio_button);

        pass.setId(passID);
        fail.setId(failID);

        int inputType = InputType.TYPE_CLASS_NUMBER;

        if (experiment.getTrialType().equals("Count")){
            inputType += InputType.TYPE_NUMBER_FLAG_SIGNED;
        } else if (experiment.getTrialType().equals("Measurement")){
            inputType += InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
        else if (experiment.getTrialType().equals("Binomial")){
            pass.setVisibility(View.VISIBLE);
            fail.setVisibility(View.VISIBLE);
        }
        value.setInputType(inputType);

        CheckBox location = findViewById(R.id.TrialLocationCheckBox);

        if(experiment.getRequireLocation()){
            location.setChecked(true);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Location was turned "+experiment.getRequireLocation()+" for this experiment.", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton generate = findViewById(R.id.generateCodeBTN);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experiment.getTrialType().equals("Binomial")){
                    if (Integer.parseInt(value.getText().toString()) > experiment.getMaxTrials()){
                        Toast.makeText(getBaseContext(), "Cannot ad more than the max trials", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String QRCodeMessage;
                    if (experiment.getTrialType().equals("Binomial")){
                        QRCodeMessage = experiment.getExpID()+","+user.getUserUniqueID()+"," + value.getText().toString() +","+experiment.getTrialType()+","+experiment.getRequireLocation() + "," + findBinoType(binoChoice.getCheckedRadioButtonId());
                    } else {
                        QRCodeMessage = experiment.getExpID() + "," + user.getUserUniqueID() + "," + value.getText().toString() + "," + experiment.getTrialType() + "," + experiment.getRequireLocation();
                    }

                    ImageView QRCode = findViewById(R.id.ReplaceImageQrCode);
                    QRCode.setImageBitmap(QRcode.generate(QRCodeMessage));
                }

                value.setText("");
            }
        });

    }//onCreate

    /**
     * This functions gets Binomial Type, because it has passes and failures
     * We use a switch case to get the binomial type
     * @param binoTypeID
     * @return boolean (either true, false, or null) based of the binomial type
     */
    public Boolean findBinoType(int binoTypeID) {
        switch (binoTypeID) {
            case passID:
                return true;
            case failID:
                return false;
            default:
                return null;
        }
    }

}
