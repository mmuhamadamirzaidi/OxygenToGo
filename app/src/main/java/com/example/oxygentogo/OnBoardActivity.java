package com.example.oxygentogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class OnBoardActivity extends AhoyOnboarderActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Jumlah Silinder", "Kira jumlah silinder yang diperlukan untuk sampai ke destinasi", R.drawable.tank128);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard1.setTitleColor(R.color.white);
        ahoyOnboarderCard1.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(5, this));

        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Durasi Silinder", "Ketahui tempoh sesuatu silinder oksigen", R.drawable.gauge128);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setTitleColor(R.color.white);
        ahoyOnboarderCard2.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard2.setTitleTextSize(dpToPixels(8, this));
        ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(5, this));

        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Lokasi Bantuan", "Dapatkan lokasi bantuan berdekatan", R.drawable.location128);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setTitleColor(R.color.white);
        ahoyOnboarderCard3.setDescriptionColor(R.color.grey_200);
        ahoyOnboarderCard3.setTitleTextSize(dpToPixels(8, this));
        ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(5, this));

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setGradientBackground();
        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("firstTime", false);
        editor.apply();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
