package it.diegocastagna.ifermi.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.DateTimeComponents;
import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.activity.AgendaActivity;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.network.DownloadFileFromURL;

public class ICSEventsExtracter extends AsyncTask<Context, Integer,  Map<CalendarDay, ArrayList<Event>>> {

    public AgendaActivity caller;

    private Model mModel = Model.getInstance();

    public ICSEventsExtracter(AgendaActivity a) {
        caller = a;
    }

    @Override
    protected Map<CalendarDay, ArrayList<Event>> doInBackground(Context... contexts) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        /*File f2 = new File(mMdodel.getCacheDir(), "agenda.ics");
        try {
            new DownloadFileFromURL().execute(f2, "https://calendar.google.com/calendar/ical/isfermimantova%40gmail.com/public/basic.ics").get();
        } catch (ExecutionException e) {
            System.out.println("download fallito");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("download fallito");
            e.printStackTrace();
        }*/
        Map<CalendarDay, ArrayList<Event>> events = new HashMap<CalendarDay, ArrayList<Event>>();
        ArrayList<EventWithDate> eventsList = new ArrayList<>();
        ICalendar ical = null;
        try {
            ical = Biweekly.parse(contexts[0].getResources().openRawResource(R.raw.agenda)).first();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < ical.getEvents().size(); i++) {
            VEvent event = ical.getEvents().get(i);
            String summary = event.getSummary().getValue();
            DateTimeComponents date = event.getDateStart().getValue().getRawComponents();
            if (date.getYear()<= year && date.getMonth()<= month && date.getDate()<day){
                break;
            }
            CalendarDay d = CalendarDay.from(date.getYear(), date.getMonth(), date.getDate());
            String time = date.getHour() + ":" + date.getMinute();
            EventWithDate e = new EventWithDate(summary, time, d);
            eventsList.add(e);
        }
        Collections.sort(eventsList);

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

    @Override
    protected void onPostExecute(Map<CalendarDay, ArrayList<Event>> agendaEvents) {
        mModel.setAgendaEvents(agendaEvents);
        caller.events = mModel.getAgendaEvents();
        caller.loading = false;
        caller.decorate();
        caller.createSpinner();
        super.onPostExecute(agendaEvents);
    }

    public class EventWithDate extends Event{
        public CalendarDay day;
        public EventWithDate(String description, String time, CalendarDay d) {
            super(description, time);
            this.day = d;
        }
    }

}
