package it.diegocastagna.ifermi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.utils.RssNews;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Model mModel;
    private LinearLayout newsContainer;
    private DrawerLayout drawer; // Drawer Layour for the Menu and the main content
    private NavigationView navView; // Navigation View aka the menu

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

        mModel = Model.getInstance(); // Model
        mModel.setCacheDir(getCacheDir());

        createViewsNews();
    }

    /**
     * Set the Container of the news with this child that should contains all the news
     * @param parent LinearLayout that will contains everything
     */
    public void setNewsContainer(LinearLayout parent){
        newsContainer.addView(parent);
    }

    /**
     * It creates the news containers
     */
    private void createViewsNews(){
        LinearLayout parent = new LinearLayout(getBaseContext());
        parent.setOrientation(LinearLayout.VERTICAL);

        ArrayList l = mModel.getNewsList();

        if(!l.isEmpty()) {
            for (Object o : l) {
                final RssNews r = (RssNews) o;

                LinearLayout imageLayout = new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageParams.setMargins(0, 30, 0, 30);
                imageLayout.setLayoutParams(imageParams);
                imageLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Children of the Parent LinearLayout
                ImageView iv = new ImageView(getBaseContext());
                downloadSetupImage(mModel.IMAGERSSURL + r.getIconId(), iv);

                LinearLayout layout = new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 0, 0, 0);
                layout.setLayoutParams(layoutParams);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Children of the layout LinearLayout
                TextView title = new TextView(getBaseContext());
                title.setText(r.getTitle());
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setTextSize(15);

                TextView description = new TextView(getBaseContext());
                description.setText(r.getShortDesc());

                TextView readMore = new TextView(getBaseContext());
                readMore.setText("Leggi tutto");
                readMore.setGravity(Gravity.END);
                readMore.setTextColor(Color.parseColor("blue"));

                parent.addView(imageLayout);
                imageLayout.addView(iv);
                imageLayout.addView(layout);
                layout.addView(title);
                layout.addView(description);
                layout.addView(readMore);

                // When you click the news, it will open it in PopupActivity
                readMore.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent i = new Intent(getBaseContext(), PopupActivity.class);
                        i.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_NEWS);
                        i.putExtra("imageUrl", mModel.IMAGERSSURL + r.getIconId());
                        i.putExtra("title", r.getTitle());
                        i.putExtra("description", r.getLongDesc());
                        startActivity(i);
                    }
                });
            }
        } else {
            TextView title = new TextView(getBaseContext());
            title.setText("Nessuna news trovata");
            parent.addView(title);
        }

        setNewsContainer(parent);
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
        } else if(v.getId() == R.id.site_logo) {
            try {
                uri = Uri.parse("https://www.fermimn.edu.it/");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
                System.out.println("[ERROR] Impossible to open the Browser");
            }
        }
    }

    /**
     * It opens the external app with that package or it will use a fallback
     * @param packageName String with the package name
     * @param fallBack String that will be used to fallback
     */
    public void openExternalApp(String packageName, String fallBack) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) { // If there's no app with that package name
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallBack));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Download the Image from Url and insert into a ImageView
     * @param imageURL URL where the image is
     * @param target Where to put it in
     */
    public void downloadSetupImage(String imageURL, ImageView target){
        Picasso.get().load(imageURL).into(target);
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
    /**
     * It opens the item selected on the Menu
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;

        switch(item.getItemId()){
            case R.id.nav_news:
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_news);
                break;
            case R.id.nav_timetable:
                intent = new Intent(getBaseContext(), OrarioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_timetable);
                break;
            case R.id.nav_calendar:
                intent = new Intent(getBaseContext(), AgendaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_calendar);
                break;
            case R.id.nav_school_calendar:
                intent = new Intent(getBaseContext(), CalendarioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_school_calendar);
                break;
            case R.id.nav_moodle:
                openExternalApp("com.moodle.moodlemobile", "https://moodle.fermi.mn.it/");
                break;
            case R.id.nav_register:
                openExternalApp("it.mastercom.parents.app", "https://fermi-mn-sito.registroelettronico.com/");
                break;
            case R.id.nav_settings:
                intent = new Intent(getBaseContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);

                navView.setCheckedItem(R.id.nav_settings);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}