package com.example.cmput301w21t23_smartdatabook.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t23_smartdatabook.Experiment;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.material.button.MaterialButton;

public class QRCodeActivity extends AppCompatActivity {
    User user = User.getUser();
    Database database = Database.getDataBase();
    QRCode QRcode = new QRCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_qr_binaomial);

//        setSupportActionBar(findViewById(R.id.app_toolbar));
//        ActionBar toolbar = getSupportActionBar();
//        assert toolbar != null;

        Intent intent = getIntent();
        Experiment experiment = (Experiment) intent.getSerializableExtra("experiment");

//        toolbar.setTitle(experiment.getExpName());

        EditText value = findViewById(R.id.passesEditTextBinomialQR);
        Switch toggle = findViewById(R.id.passFail);

        if(experiment.getTrialType().equals("Binomial")){
            toggle.setVisibility(View.VISIBLE);
        }

        CheckBox location = findViewById(R.id.binomialTrialLocationCheckBox);

        MaterialButton generate = findViewById(R.id.generateCodeBTNBinomial);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QRCodeMessage = experiment.getExpID()+","+user.getUserUniqueID()+","+value.getText().toString();

                value.setHint("# value");

                ImageView QRCode = findViewById(R.id.binomialReplaceImageQrCode);

                QRCode.setImageBitmap(QRcode.generate(QRCodeMessage));
            }
        });



    }
}
