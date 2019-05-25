package it.diegocastagna.ifermi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.utils.Event;

/**
 * Class that is used to show Popups in other classes
 */
public class PopupActivity extends Activity {
    public final static int TYPE_CALENDARIO = 0;
    public final static int TYPE_AGENDA = 1;
    public final static int TYPE_NEWS = 2;
    public final static String TYPE_STRING = "type";

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent intent = getIntent();
            Model mModel = Model.getInstance(); // Model
            int type = intent.getIntExtra(TYPE_STRING, TYPE_CALENDARIO);
            switch (type){
                case 0: // Case Calendario
                    setContentView(R.layout.activity_popup);

                    Map<CalendarDay, String> events = mModel.getCalendarEvents();
                    CalendarDay selectedDate = new Gson().fromJson(intent.getStringExtra("data"), CalendarDay.class);

                    if(events.get(selectedDate) != null)
                        ((TextView) findViewById(R.id.popup)).setText(events.get(selectedDate));
                    else
                        ((TextView) findViewById(R.id.popup)).setText("Non ci sono eventi");
                    break;
                case 1: // Case Agenda
                    setContentView(R.layout.activity_scrollable_popup);

                    ArrayList<Event> agendaEvents = new ArrayList<>();

                    selectedDate = new Gson().fromJson(intent.getStringExtra("data"), CalendarDay.class);
                    String selectedClass = intent.getStringExtra("className");
                    if(selectedClass.equals("Tutti gli eventi"))
                        agendaEvents = mModel.getAgendaEventsOnDate(selectedDate);
                    else
                        agendaEvents = mModel.getAgendaClassEventsOnDate(selectedDate, selectedClass);

                    if(!agendaEvents.isEmpty())
                        for (Event e: agendaEvents)
                            ((TextView) findViewById(R.id.popup)).append("\u2022 (" + e.getTime() + ") " + e.getDescription() + "\n");
                    else
                        ((TextView) findViewById(R.id.popup)).setText("Non ci sono eventi");
                    break;
                case 2: // Case News
                    setContentView(R.layout.activity_main_news_full);
                    String imageURL = intent.getStringExtra("imageURL");
                    String title = intent.getStringExtra("title");
                    String desc = intent.getStringExtra("description");

                    ImageView iv = findViewById(R.id.news_image);
                    TextView titleView = findViewById(R.id.news_title);
                    TextView descView = findViewById(R.id.news_description);

                    titleView.setText(title);
                    descView.setText(desc);

                    Picasso.get().load(imageURL).into(iv);
                    break;
            }

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            getWindow().setLayout((int) (dm.widthPixels * .85), ActionBar.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.dimAmount = 0.75f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(layoutParams);
        }
}
