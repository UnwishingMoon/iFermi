package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.os.Bundle;

import it.diegocastagna.ifermi.R;

public class StudentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
    }

    // it's for "Zona studenti"
}
