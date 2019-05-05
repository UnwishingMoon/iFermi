package it.diegocastagna.ifermi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {
    private Model mModel;
    private DrawerLayout drawerLayout; // Drawer of the Entire Activity
    private TextView msgWelcome; // welcome user message that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mModel = Model.getInstance();
        this.mModel.addObserver(this);

        this.drawerLayout = findViewById(R.id.drawer_layout); // Create the Drawer Layour for the Menu and the main content
        this.msgWelcome = findViewById(R.id.msg_welcome); // TextView that will fade away after X seconds
    }

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