package it.diegocastagna.ifermi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class PopupActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_popup);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = dm.heightPixels;


            String selectedDate = getIntent().getStringExtra("data");
            TextView popup = findViewById(R.id.popup);
            popup.setText(selectedDate);

            getWindow().setLayout((int) (width * .8), (int) (height * .8));

        }

}
