package it.diegocastagna.ifermi.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.network.RssNews;

public class MainActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener {
    private Model mModel;
    private LinearLayout newsContainer;
    private TextView msgWelcome; // welcome user message that
    private DrawerLayout drawer;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout); // Create the Drawer Layour for the Menu and the main content
        navView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer , R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        newsContainer = findViewById(R.id.news_container); // Linearlayout that should contain news
        msgWelcome = findViewById(R.id.msg_welcome); // TextView that will fade away after X seconds

        mModel = Model.getInstance();
        mModel.setCacheDir(getCacheDir());
        mModel.addObserver(this);

        List l = mModel.getNewsList();
        for(Object o : l){
            RssNews r = (RssNews) o;

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;

        switch(item.getItemId()){
            case R.id.nav_news:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_news);
                break;
            case R.id.nav_timetable:
                intent = new Intent(getApplicationContext(), OrarioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_timetable);
                break;
            case R.id.nav_calendar:
                intent = new Intent(getApplicationContext(), AgendaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_calendar);
                break;
            case R.id.nav_school_calendar:
                intent = new Intent(getApplicationContext(), CalendarioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_school_calendar);
                break;
            case R.id.nav_moodle:
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setComponent(new ComponentName("com.moodle.moodlemobile","com.moodle.moodlemobile.MainActivity"));
                startActivity(i);

                navView.setCheckedItem(R.id.nav_moodle);
                break;
            case R.id.nav_register:
                intent = new Intent();
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_register);
                break;
            case R.id.nav_settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_settings);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * It tryes to open an app that can handle GPS informations like Maps
     * @param v The view that called this function
     */
    public void openGeoIntent(View v){
        Intent intent;
        Uri uri;

        // If the action is the address map
        if(v.getId() == R.id.school_address) {
            try {
                uri = Uri.parse("geo:45.1412746,10.7645877,17z?q=" + Uri.encode("Istituto Enrico Fermi Mantova"));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
                System.out.println("[ERROR] Impossible to open Maps");
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}