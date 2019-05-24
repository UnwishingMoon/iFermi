package it.diegocastagna.ifermi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;

public class CalendarioActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content_frame);
        getLayoutInflater().inflate(R.layout.activity_calendario, layout);

        final MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date = findViewById(R.id.date);
        Map<CalendarDay, String> events ;
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();


        Model mModel = Model.getInstance(); // Model
        try {
            if (mModel.updateCalendarEvents(this)){
                events = mModel.getCalendarEvents();
                for (CalendarDay name: events.keySet()){
                    dates.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        calendarView.addDecorator(new CalendarioActivity.EventDecorator(0xFFFF0000,dates));

        calendarView.setOnDateChangedListener(
                new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay d, boolean selected) {
                        Intent intent = new Intent(CalendarioActivity.this, PopupActivity.class);
                        intent.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_CALENDARIO);
                        intent.putExtra("data",new Gson().toJson(d));
                        startActivity(intent);
                    }
                }
        );

    }

    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
}
