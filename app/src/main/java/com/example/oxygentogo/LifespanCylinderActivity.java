package com.example.oxygentogo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import mehdi.sakout.fancybuttons.FancyButton;

public class LifespanCylinderActivity extends BaseActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        DiscreteSeekBar.OnProgressChangeListener {

    @BindView(R.id.button_calculate)
    FancyButton buttonCalculate;

    @BindView(R.id.spinner_cylinder_type)
    Spinner spinnerCylinder;

    @BindView(R.id.spinner_mask_type)
    Spinner spinnerMask;

    @BindView(R.id.editText_pressureLeft)
    EditText editTextPressureLeft;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.seekBar)
    DiscreteSeekBar seekBar;

    @BindView(R.id.textView_indicator)
    TextView textViewIndicator;

    private int flowRate = 1, cylinderVolume = 500;
    private AlertDialog ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifespan_cylinder);

        toolbar.setTitle("Durasi Silinder");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> listCylinder = new ArrayList<>();
        listCylinder.add("Silinder D");
        listCylinder.add("Silinder E");
        listCylinder.add("Silinder F");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCylinder);
        spinnerCylinder.setAdapter(adapter);

        List<String> listMask = new ArrayList<>();
        listMask.add("Nasal Prong: 1-5L");
        listMask.add("Face Mask: 5-8L");
        listMask.add("Highflow Mask: 8-15L");
        listMask.add("Ventilator: 25L");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMask);
        spinnerMask.setAdapter(adapter2);

        buttonCalculate.setOnClickListener(this);

        spinnerCylinder.setOnItemSelectedListener(this);
        spinnerMask.setOnItemSelectedListener(this);
        seekBar.setOnProgressChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_calculate:
                int pressureLeft = 0;
                try {
                    pressureLeft = Integer.parseInt(editTextPressureLeft.getText().toString());
                    if (pressureLeft < 0 || pressureLeft > 2000) {
                        new AlertDialog.Builder(this)
                                .setTitle("Jarak nombor salah")
                                .setMessage("Sila masukkan nombor 0 hingga 2000 sahaja")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        showResult();
                    }
                } catch (NumberFormatException e) {
                    new AlertDialog.Builder(this)
                            .setTitle("Format salah")
                            .setMessage("Sila masukkan nombor 0 hingga 2000 sahaja")
                            .setPositiveButton("OK", null)
                            .show();
                }

                break;
            case R.id.button_close:
                ad.dismiss();
                break;

        }
    }

    public void showResult() {
        String duration = "";

        double pressureLeft = 0;
        try {
            pressureLeft = Double.parseDouble(editTextPressureLeft.getText().toString());
        } catch (NumberFormatException e) {
            Log.e("Error", e.toString());
        }

        double first = (double) (getVolumeForCylinder(spinnerCylinder.getSelectedItemPosition())) / (double) 2000;
        double min = first * pressureLeft / flowRate;
        int minuteLeft = (int) Math.floor(min);

        duration = minuteLeft + " minit";

        int hour = minuteLeft / 60;
        minuteLeft = minuteLeft % 60;

        if (hour > 0) {
            duration = hour + " jam " + minuteLeft + " minit";
        } else {
            duration = minuteLeft + " minit";
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.adb_lifespan_result, null);
        adb.setView(view);

        FancyButton buttonClose = (FancyButton) view.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(this);

        TextView textViewResult = (TextView) view.findViewById(R.id.textView_result);
        textViewResult.setText(duration);

        ad = adb.create();
        ad.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_mask_type:
                if (position == 0) {
                    flowRate = 1;
                    textViewIndicator.setText("1L/min");
                    seekBar.setProgress(0);
                    seekBar.setMin(1);
                    seekBar.setMax(5);
                    seekBar.setProgress(1);
                } else if (position == 1) {
                    flowRate = 5;
                    textViewIndicator.setText("5L/min");
                    seekBar.setProgress(0);
                    seekBar.setMin(5);
                    seekBar.setMax(8);
                    seekBar.setProgress(5);
                } else if (position == 2) {
                    flowRate = 8;
                    textViewIndicator.setText("8L/min");
                    seekBar.setProgress(0);
                    seekBar.setMin(8);
                    seekBar.setMax(15);
                    seekBar.setProgress(8);
                } else if (position == 3) {
                    flowRate  = 25;
                    textViewIndicator.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    textViewIndicator.setText("25L/min");
                }
                break;
            case R.id.spinner_cylinder_type:
                if (position == 0) {
                    cylinderVolume = 500;
                } else if (position == 1) {
                    cylinderVolume = 700;
                } else if (position == 2) {
                    cylinderVolume = 1400;
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        textViewIndicator.setText(String.format(Locale.getDefault(), "%dL/min", value));
        flowRate = value;

    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    public int getVolumeForCylinder(int position) {
        int value = 0;
        if (position == 0) {
            value = 500;
        } else if (position == 1) {
            value = 700;
        } else if (position == 2) {
            value = 1400;
        }
        return value;
    }
}
