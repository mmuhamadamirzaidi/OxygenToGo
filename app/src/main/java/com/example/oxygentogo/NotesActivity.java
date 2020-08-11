package com.example.oxygentogo;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import butterknife.BindView;

public class NotesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
//
        toolbar.setTitle(getString(R.string.references_notes));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
