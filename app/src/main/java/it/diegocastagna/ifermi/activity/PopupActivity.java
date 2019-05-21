package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import it.diegocastagna.ifermi.R;

public class PopupActivity extends Activity {
    public final static int TYPE_CALENDARIO = 0;
    public final static int TYPE_AGENDA = 1;
    public final static int TYPE_NEWS = 2;
    public final static String TYPE_STRING = "type";

    @Override
        protected void onCreate(Bundle savedInstanceState) { // Tutto tuo Kunal
            super.onCreate(savedInstanceState);

            Intent intent = getIntent();
            int type = intent.getIntExtra(TYPE_STRING, TYPE_CALENDARIO);
            switch (type){
                case 0: // Case Calendario
                    setContentView(R.layout.activity_popup);

                    String selectedDate = intent.getStringExtra("data");
                    ((TextView) findViewById(R.id.popup)).setText(selectedDate);
                    break;
                case 1: // Case Agenda
                    setContentView(R.layout.activity_popup);

                    selectedDate = intent.getStringExtra("data");
                    ((TextView) findViewById(R.id.popup)).setText(selectedDate);
                    break;
                case 2: // Case News
                    setContentView(R.layout.activity_main_news_full);
                    String imageURL = intent.getStringExtra("imageURL");
                    String title = intent.getStringExtra("title");
                    String desc = intent.getStringExtra("description");


                    break;
            }

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            getWindow().setLayout((int) (dm.widthPixels * .85), (int) (dm.heightPixels * .85));
        }
}
