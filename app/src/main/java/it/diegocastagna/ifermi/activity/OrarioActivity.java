package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import it.diegocastagna.ifermi.R;

public class OrarioActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content);
        getLayoutInflater().inflate(R.layout.activity_orario, layout);
    }
}
