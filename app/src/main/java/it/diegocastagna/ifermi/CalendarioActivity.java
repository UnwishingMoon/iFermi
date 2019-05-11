package it.diegocastagna.ifermi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarioActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date =  findViewById(R.id.date);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                String selectedDate = (i1+1) + "/" + i2 + "/" + i;
                date.setText(selectedDate);
                Intent intent = new Intent(CalendarioActivity.this, PopupActivity.class);
                intent.putExtra("data",date.getText().toString());
                startActivity(intent);
            }
        });
    }
}
