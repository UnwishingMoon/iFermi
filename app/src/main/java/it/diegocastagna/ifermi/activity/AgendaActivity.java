package it.diegocastagna.ifermi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
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

public class AgendaActivity extends MainActivity implements AdapterView.OnItemSelectedListener {
    public Model mModel = Model.getInstance(); // Model
    public Map<CalendarDay, ArrayList<Event>> events ;
    public HashSet<CalendarDay> dates = new HashSet<CalendarDay>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = findViewById(R.id.activity_main_content_frame);
        getLayoutInflater().inflate(R.layout.activity_agenda, layout);
        final TextView date = findViewById(R.id.date);
        date.setText("Download in corso delle variazioni giornaliere");
        Map<CalendarDay, ArrayList<Event>> events ;
        HashSet<CalendarDay> dates = new HashSet<CalendarDay>();
        final MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        final Spinner dropdown = findViewById(R.id.spinner);
        Model mModel = Model.getInstance();


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
                        intent.putExtra("className" , dropdown.getSelectedItem().toString());
                        intent.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_AGENDA);
                        intent.putExtra("data",new Gson().toJson(d));
                        startActivity(intent);
                    }
                }
        );

    }

    public void createSpinner(){
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Tutti gli eventi", "1A" , "1ACH" , "1AEL" , "1AIN" , "1AME" , "1B" , "1BEL" , "1BIN" , "1BME" , "1C" , "1CELCH" , "1CIN" , "1CME" , "1D" , "1DIN" , "1DME" , "1E" , "2ACH" , "2AIN" , "2AME" , "2B" , "2BEL" , "2BME" , "2C" , "2CEL" , "2CIN" , "2CME" , "2D" , "2DIN" , "2E" , "2EIN" , "2F" , "3A" , "3AMME" , "3B" , "3BMME" , "3C" , "3CBIOMENE" , "3C BIO" , "3MENE" , "3CCH" , "3D" , "3E" , "3EAU" , "3EELEET" , "3EELE" , "3EET" , "3F" , "3IIN" , "3ITEL" , "4AMME" , "4B" , "4BMME" , "4C" , "4CCHCBIO" , "4CBIO" , "4CCH" , "4D" , "4E" , "4EAU" , "4EELE" , "4EET" , "4IIN" , "4ITEL" , "4MENE" , "5A" , "5B" , "5C" , "5CBIO" , "5C CH" , "5D" , "5E" , "5EAUET" , "5EAU" , "5EET" , "5EELE" , "5IIN", "5ITEL" , "5MENE" , "5MME"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setVisibility(View.VISIBLE);
        final TextView date = findViewById(R.id.date);
        date.setText("Variazioni giornaliere");
        dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        if(position!=0){
            events = mModel.getAgendaClassEvents(parent.getItemAtPosition(position).toString());
            decorate();
        }else{
            events = mModel.getAgendaEvents();
            decorate();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void decorate(){
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        dates.clear();
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
