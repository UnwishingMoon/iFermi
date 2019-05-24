package it.diegocastagna.ifermi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.utils.Event;

public class AgendaActivity extends MainActivity {
    private static AgendaActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content_frame);
        getLayoutInflater().inflate(R.layout.activity_agenda, layout);
        final TextView date = findViewById(R.id.date);
        Map<CalendarDay, ArrayList<Event>> events ;
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();
        final MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        Model mModel = Model.getInstance(); // Model
        try {
            mModel.updateAgendaEvents(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendarView.removeDecorators();
        calendarView.addDecorator(new EventDecorator(0xFFFF0000,dates));

        calendarView.setOnDateChangedListener(
                new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay d, boolean selected) {
                        Intent intent = new Intent(AgendaActivity.this, PopupActivity.class);
                        intent.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_AGENDA);
                        intent.putExtra("data",new Gson().toJson(d));
                        startActivity(intent);
                    }
                }
        );

    }

    public void decorate(){
        Map<CalendarDay, ArrayList<Event>> events ;
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();
        Model mModel = Model.getInstance(); // Model
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        events = mModel.getAgendaEvents();
        for (CalendarDay name: events.keySet()){
            dates.add(name);
        }
        calendarView.removeDecorators();
        calendarView.addDecorator(new EventDecorator(0xFFFF0000,dates));
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
