package com.richard.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements OnEditorActionListener {

    //define instance variables
    private EditText weightEditText;
    private TextView milesRanTextView;
    private SeekBar milesRanSeekBar;
    private TextView caloriesBurnedTextView;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private TextView bmiTextView;
    private EditText nameEditText;

    private SharedPreferences savedValues;
    private String weightString;
    private String milesRanString;
    private String feetString;
    private String inchesString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get references to widgets
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        milesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);


        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);


        //set listeners
        weightEditText.setOnEditorActionListener(this);

        SeekBarListener seekBarListener = new SeekBarListener();
        milesRanSeekBar.setOnSeekBarChangeListener(seekBarListener);


        //feet spinner list
        List<String> feet = new ArrayList<String>();
        feet.add("3");
        feet.add("4");
        feet.add("5");
        feet.add("6");
        feet.add("7");
        // Creating adapter for feet spinner
        ArrayAdapter<String> feetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, feet);
        // Drop down layout style
        feetAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // attaching feet adapter to spinner
        feetSpinner.setAdapter(feetAdapter);


        //inches spinner list
        List<String> inches = new ArrayList<String>();
        inches.add("0");
        inches.add("1");
        inches.add("2");
        inches.add("3");
        inches.add("4");
        inches.add("5");
        inches.add("6");
        inches.add("7");
        inches.add("8");
        inches.add("9");
        inches.add("10");
        inches.add("11");
        // Creating adapter for inches spinner
        ArrayAdapter<String> inchesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, inches);
        // Drop down layout style
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // attaching inches adapter to spinner
        inchesSpinner.setAdapter(inchesAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //editor action listener
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    //seekbar listener
    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            milesRanTextView.setText(progress  + "mi");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    public void calculateAndDisplay() {

        //get weight and miles ran values
        weightString = weightEditText.getText().toString();
        float weight = Float.parseFloat(weightString);

        String milesRanString = milesRanTextView.getText().toString();
        String milesRanString2 = milesRanString.substring(0, milesRanString.length()-2);
        float milesRan = Integer.parseInt(milesRanString2);

        //calculate calories burned
        float caloriesBurned = 0.75f * weight * milesRan;

        //set calories burned text
        NumberFormat calories = NumberFormat.getInstance();
        caloriesBurnedTextView.setText(calories.format(caloriesBurned));


        //get height values
        feetString = feetSpinner.getSelectedItem().toString();
        int feet = Integer.parseInt(feetString);

        inchesString = inchesSpinner.getSelectedItem().toString();
        int inches = Integer.parseInt(inchesString);

        //calculate bmi
        float bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        //set bmi text
        NumberFormat bmiText = NumberFormat.getInstance();
        bmiTextView.setText(bmiText.format(bmi));



    }


    @Override
    protected void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("weightString", weightString);
        editor.putString("milesRanString", milesRanString);
        editor.putString("feetString", feetString);
        editor.putString("inchesString", inchesString);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        weightString = savedValues.getString("weightString", "000");
        weightEditText.setText(weightString);

        milesRanString = savedValues.getString("milesRanString", "1mi");
        milesRanTextView.setText(milesRanString); //doesnt work for some reason

        String feet = savedValues.getString("feetString", "5");
        int f = Integer.parseInt(feet);
        feetSpinner.setSelection(f-3);

        String inches = savedValues.getString("inchesString", "6");
        int i= Integer.parseInt(inches);
        inchesSpinner.setSelection(i);

        //calculateAndDisplay();

        super.onResume();
    }
}
