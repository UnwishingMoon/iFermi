package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Map;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;

/**
 * Class that is used to show Popups in other classes
 */
public class PopupActivity extends Activity {
    public final static int TYPE_CALENDARIO = 0;
    public final static int TYPE_AGENDA = 1;
    public final static int TYPE_NEWS = 2;
    public final static String TYPE_STRING = "type";

    @Override
        protected void onCreate(Bundle savedInstanceState) { // Tutto tuo Kunal
            super.onCreate(savedInstanceState);
            Intent intent = getIntent();
            Model mModel = Model.getInstance(); // Model
            int type = intent.getIntExtra(TYPE_STRING, TYPE_CALENDARIO);
            switch (type){
                case 0: // Case Calendario
                    setContentView(R.layout.activity_popup);

                    Map<CalendarDay, String> events = mModel.getCalendarEvents();
                    CalendarDay selectedDate = new Gson().fromJson(intent.getStringExtra("data"), CalendarDay.class);
                    String selectedDateString = selectedDate.getDay() + "/" +  selectedDate.getMonth() + "/" + (selectedDate.getYear()+1) ;
                    ((TextView) findViewById(R.id.popup)).setText(events.get(selectedDate));
                    break;
                case 1: // Case Agenda
                    setContentView(R.layout.activity_popup);

                    selectedDate = new Gson().fromJson(intent.getStringExtra("data"), CalendarDay.class);
                    selectedDateString = selectedDate.getDay() + "/" +  selectedDate.getMonth() + "/" + (selectedDate.getYear()+1) ;
                    ((TextView) findViewById(R.id.popup)).setText(selectedDateString);
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

            getWindow().setLayout((int) (dm.widthPixels * .85), (int) (dm.heightPixels * .80));
        }
}
