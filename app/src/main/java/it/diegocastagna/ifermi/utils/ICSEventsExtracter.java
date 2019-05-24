package it.diegocastagna.ifermi.utils;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.DateTimeComponents;
import it.diegocastagna.ifermi.R;

public class ICSEventsExtracter {
    public Map<CalendarDay, Event> extractEvents(Context context) throws IOException {
        Map<CalendarDay, Event> events = new HashMap<CalendarDay, Event>();
        ICalendar ical = Biweekly.parse(context.getResources().openRawResource(R.raw.agenda)).first();
        for(int i=0; i< ical.getEvents().size();i++){
            VEvent event = ical.getEvents().get(i);
            String summary = event.getSummary().getValue();
            DateTimeComponents date = event.getDateStart().getValue().getRawComponents();
            CalendarDay d = CalendarDay.from(date.getYear(), date.getMonth(), date.getDate());
            String time = date.getHour() + ":" + date.getMinute();
            Event e = new Event(summary, time);
            events.put(d, e);
        }
        return events;
    }
}
