package it.diegocastagna.ifermi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener {
    private Model mModel;
    private LinearLayout newsContainer;
    private TextView msgWelcome; // welcome user message that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout); // Create the Drawer Layour for the Menu and the main content
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name , R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

        switch(item.getItemId()){
            case R.id.nav_news:
                
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}