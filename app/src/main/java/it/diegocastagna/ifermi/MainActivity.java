package it.diegocastagna.ifermi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private DrawerLayout drawerLayout; // Drawer of the Entire Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout); // Create the Drawer Layour for the Menu and the main content

        // TODO Retrieve user name from Files(?)
        /*TextView txt_view = (TextView) findViewById(R.id.msg_user);
        txt_view.setText(user_name); // Set the Welcome user message to the user_name
        txt_view.setVisibility(View.VISIBLE); // Set it Visible*/
    }

    public void openIntent(View v){
        Intent intent;
        Uri uri;

        // If the action is the address map
        if(v.getId() == R.id.school_address){
            try{
                uri = Uri.parse("geo:45.1412746,10.7645877,17z?q=" + Uri.encode("Istituto Enrico Fermi Mantova"));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }catch(Exception e){

            }
        }
        // It opens the Dialer with the School Phone already typed in
    }
}