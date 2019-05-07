package it.diegocastagna.ifermi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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

public class MainActivity extends AppCompatActivity implements Observer {
    private Model mModel;
    private DrawerLayout drawerLayout; // Drawer of the Entire Activity
    private LinearLayout newsContainer;
    private TextView msgWelcome; // welcome user message that
    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);

        drawerLayout = findViewById(R.id.drawer_layout); // Create the Drawer Layour for the Menu and the main content
        msgWelcome = findViewById(R.id.msg_welcome); // TextView that will fade away after X seconds
        newsContainer = findViewById(R.id.news_container); // Linearlayout that should contain news

        mModel = Model.getInstance();
        mModel.setCacheDir(getCacheDir());
        mModel.addObserver(this);

        List l = mModel.getNewsList();
        for(Object o : l){
            RssNews r = (RssNews) o;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate( R.menu.menu_toolbar, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.toolbar_action_menu:
                // User chose the "Menu" item, show the app Menu
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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