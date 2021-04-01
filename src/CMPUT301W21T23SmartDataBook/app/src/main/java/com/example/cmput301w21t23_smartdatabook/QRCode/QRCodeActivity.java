package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.common.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeActivity extends AppCompatActivity {
    User user = User.getUser();
    QRCode QRcode = new QRCode();
    private static final int passID = 1;
    private static final int failID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_qr);

//        setSupportActionBar(findViewById(R.id.app_toolbar));
//        ActionBar toolbar = getSupportActionBar();
//        assert toolbar != null;

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

//        toolbar.setTitle(experiment.getExpName());

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

        if(experiment.getRegionOn()){
            location.setChecked(true);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Location was turned "+experiment.getRegionOn()+" for this experiment.", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton generate = findViewById(R.id.generateCodeBTN);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String QRCodeMessage;
                Log.d("Value", "" + value.getText().toString());

                if (experiment.getTrialType().equals("Binomial")){

                    QRCodeMessage = experiment.getExpID()+","+user.getUserUniqueID()+"," + value.getText().toString() +","+experiment.getTrialType()+","+experiment.getRegionOn() + "," + findBinoType(binoChoice.getCheckedRadioButtonId());

                } else {

                    QRCodeMessage = experiment.getExpID() + "," + user.getUserUniqueID() + "," + value.getText().toString() + "," + experiment.getTrialType() + "," + experiment.getRegionOn();

                }

                ImageView QRCode = findViewById(R.id.ReplaceImageQrCode);
                QRCode.setImageBitmap(QRcode.generate(QRCodeMessage));
                value.setText("");
            }
        });

    }//onCreate

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
