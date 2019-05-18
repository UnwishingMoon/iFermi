package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import it.diegocastagna.ifermi.R;

public class PopupActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) { // Tutto tuo Kunal
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_popup);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = dm.heightPixels;

            String selectedDate = getIntent().getStringExtra("data");
            TextView popup = findViewById(R.id.popup);
            popup.setText(selectedDate);

            getWindow().setLayout((int) (width * .85), (int) (height * .85));
        }
}
