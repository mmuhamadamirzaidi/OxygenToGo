package com.example.oxygentogo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.container)
    LinearLayout container;

    @BindView(R.id.textView_appName)
    TextView textViewAppName;

    @BindView(R.id.textView_appName2)
    TextView textViewAppName2;

    @BindView(R.id.textView_appName3)
    TextView textViewAppName3;

    Handler handler;
    Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (sp.getBoolean("firstTime", true)) {
                    i = new Intent(SplashScreenActivity.this, OnBoardActivity.class);

                } else {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                }

                startActivity(i);
                finish();
            }
        };

        handler.postDelayed(runnable, 5000);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Sensations and Qualities.ttf");
        textViewAppName.setTypeface(face);
        textViewAppName2.setTypeface(face);
        textViewAppName3.setTypeface(face);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Calendar cal1 = Calendar.getInstance();
//        Date d1 = cal1.getTime();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        try {
//            Date strDate = sdf.parse("22/06/2017");
//            if (d1.before(strDate)) {
//                handler.postDelayed(runnable, 5000);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}