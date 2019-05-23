package it.diegocastagna.ifermi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONObject;

import java.util.Calendar;

import it.diegocastagna.ifermi.R;

public class AgendaActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content_frame);
        getLayoutInflater().inflate(R.layout.activity_agenda, layout);
        final MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date = findViewById(R.id.date);

        calendarView.setOnDateChangedListener(
                new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay d, boolean selected) {
                        String selectedDate =  d.getDay() + "/" + d.getMonth() + "/" + (d.getYear()+1);

                        date.setText(selectedDate);
                        Intent intent = new Intent(AgendaActivity.this, PopupActivity.class);
                        intent.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_AGENDA);
                        intent.putExtra("data",new Gson().toJson(d));
                        startActivity(intent);
                    }
                }
        );

    }
}
