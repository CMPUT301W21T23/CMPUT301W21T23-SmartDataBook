package com.example.cmput301w21t23_smartdatabook.trials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.cmput301w21t23_smartdatabook.R;
import com.example.cmput301w21t23_smartdatabook.database.Database;
import com.example.cmput301w21t23_smartdatabook.database.GeneralDataCallBack;
import com.example.cmput301w21t23_smartdatabook.user.User;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Class: TrialList
 * This class consists of a list of trials
 * @author Afaq Nabi, Jayden Cho
 * @See Trial
 */
public class TrialList extends ArrayAdapter<Trial> {
    private final ArrayList<Trial> trials;
    private final Context context;

    /**
     * TrialList's public constructor
     * @param trials
     * @param context
     */
    public TrialList(ArrayList<Trial> trials, Context context) {
        super(context,0, trials);
        this.trials = trials;
        this.context = context;
    }

    /**
     * This function sets up the visual representation of the trial list, and set trialValue
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Trial trial = trials.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.trial_card, parent,false);
        }
        TextView trialValue = convertView.findViewById(R.id.trial_value);
        if (trial.getExpType().equals("Binomial")){
            Boolean val = (Boolean) trial.getValue();
            trialValue.setText(val.toString());
        }
        else {
            trialValue.setText( trial.getValue().toString());
        }

        TextView trialDate = convertView.findViewById(R.id.trial_date);
        trialDate.setText(trial.getDate());

        TextView trialAuthor = convertView.findViewById(R.id.trial_author);

        new Database().fillUserName(new GeneralDataCallBack() {
			@Override
			public void onDataReturn(Object returnedObject) {
				Hashtable<String, User> UserName = (Hashtable<String, User>) returnedObject;
                trialAuthor.setText(UserName.get(trial.getUid()).getUserName());
			}
		});

        return convertView;
    }
}
