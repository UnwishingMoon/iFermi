package it.diegocastagna.ifermi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;

import it.diegocastagna.ifermi.R;

public class AgendaActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content);
        getLayoutInflater().inflate(R.layout.activity_agenda, layout);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date = findViewById(R.id.date);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                String selectedDate = i2 + "/" + (i1+1) + "/" + i;
                date.setText(selectedDate);
                Intent intent = new Intent(AgendaActivity.this, PopupActivity.class);
                intent.putExtra("data",date.getText().toString());
                startActivity(intent);
            }
        });
    }
}
