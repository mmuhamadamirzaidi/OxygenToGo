package com.example.oxygentogo;

import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements
        View.OnClickListener {

    @BindView(R.id.button_1)
    Button button1;
    @BindView(R.id.button_2)
    Button button2;
    @BindView(R.id.button_3)
    Button button3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cardView1)
    CardView cardView1;
    @BindView(R.id.cardView2)
    CardView cardView2;
    @BindView(R.id.cardView3)
    CardView cardView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.cardView1:
                intent = new Intent(this, TotalCylinderActivity.class);
                startActivity(intent);
                break;
            case R.id.cardView2:
                intent = new Intent(this, LifespanCylinderActivity.class);
                startActivity(intent);
                break;
            case R.id.cardView3:
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
