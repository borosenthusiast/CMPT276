package com.t.teamten.greenfoodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_DROPDOWNS = 7;
    private TextView mIntroText;
    private Switch mDataEntrySwapSwitch;
    private ArrayList<Spinner> mSpinnerArray;
    private ArrayList<EditText> mEditTextArray;
    private Button mResultsButton;
    private static final int SET_TO_NUMBER = 1;
    private static final int SET_TO_PERCENT = 0;
    private int switchStatus = SET_TO_PERCENT;
    public static final String DATA_PASSED = "ResultStoredAsObject";
    // for use in determining how data is entered by the user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextArray = new MainActivityArrayHandler(this).populateEditTextArray();
        mSpinnerArray =  new MainActivityArrayHandler(this).populateSpinnerArray();

        mResultsButton = findViewById(R.id.resultsButton);
        mResultsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //submit result function, load up the values in each and send it to the next activity.
                CalculatorActivityData sendToResultsActivity;
                Integer editTextValue;
                String spinnerValue;
                ArrayList<Pair> pairArraySentToActivity = new ArrayList<>();
                Pair<String, Integer> resultPair;
                for (int i = 0; i < MAX_DROPDOWNS; i++) {
                    if (!mEditTextArray.get(i).getText().toString().isEmpty() &&
                            !mSpinnerArray.get(i).getSelectedItem().toString().isEmpty()
                            && mEditTextArray.get(i).getText() != null) {
                        //unentered edittexts and spinners will not be added to the array

                        editTextValue = Integer.valueOf(mEditTextArray.get(i).getText().toString());
                        spinnerValue = mSpinnerArray.get(i).getSelectedItem().toString();
                        resultPair = new Pair<>(spinnerValue, editTextValue);
                        pairArraySentToActivity.add(resultPair);
                    }
                }
                sendToResultsActivity = new CalculatorActivityData(pairArraySentToActivity, switchStatus);
                Intent intent = new Intent(MainActivity.this, ResultScreenFirst.class);
                intent.putExtra(DATA_PASSED, sendToResultsActivity);
                startActivity(intent);
            }
        });

        mIntroText = findViewById(R.id.introInstructionText);
        mDataEntrySwapSwitch = findViewById(R.id.dataEntrySwapSwitch);
        mDataEntrySwapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIntroText.setText(R.string.intro_description_percentage);
                    switchStatus = SET_TO_PERCENT;
                }
                else {
                    mIntroText.setText(R.string.intro_description_number);
                    switchStatus = SET_TO_NUMBER;
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food_array, android.R.layout.simple_spinner_dropdown_item);
        for(Spinner currentSpinner : mSpinnerArray) { //sets the items to be listed in each drop down menu
            currentSpinner.setAdapter(adapter);
        }
    }
}
