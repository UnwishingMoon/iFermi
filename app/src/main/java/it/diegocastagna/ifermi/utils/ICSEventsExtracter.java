package it.diegocastagna.ifermi.utils;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.DateTimeComponents;
import it.diegocastagna.ifermi.R;

public class ICSEventsExtracter {
    public Map<CalendarDay, ArrayList<Event>> extractEvents(Context context) throws IOException {
        Map<CalendarDay, ArrayList<Event>> events = new HashMap<CalendarDay, ArrayList<Event>>();
        ArrayList<EventWithDate> eventsList = new ArrayList<>();
        ICalendar ical = Biweekly.parse(context.getResources().openRawResource(R.raw.agenda)).first();
        for (int i = 0; i < ical.getEvents().size(); i++) {
            VEvent event = ical.getEvents().get(i);
            String summary = event.getSummary().getValue();
            DateTimeComponents date = event.getDateStart().getValue().getRawComponents();
            CalendarDay d = CalendarDay.from(date.getYear(), date.getMonth(), date.getDate());
            String time = date.getHour() + ":" + date.getMinute();
            EventWithDate e = new EventWithDate(summary, time, d);
            eventsList.add(e);
        }
        for (EventWithDate e : eventsList){
            if(events.get(e.day) != null)
                events.get(e.day).add(new Event(e.getDescription(), e.getTime()));
            else{
                events.put(e.day, new ArrayList<Event>());
                events.get(e.day).add(new Event(e.getDescription(), e.getTime()));
            }
        }
        return events;
    }

    public class EventWithDate extends Event{
        public CalendarDay day;
        public EventWithDate(String description, String time, CalendarDay d) {
            super(description, time);
            this.day = d;
        }
    }
}
