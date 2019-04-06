package it.diegocastagna.ifermi;

import android.app.Activity;
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

        drawerLayout = findViewById(R.id.drawer_layout);

        // TODO Retrieve user name from Files(?)
        /*TextView txt_view = (TextView) findViewById(R.id.msg_user);
        txt_view.setText(user_name); // Set the Welcome user message to the user_name
        txt_view.setVisibility(View.VISIBLE); // Set it Visible*/
    }

    public void openDialer(View v){
        // It opens the Dialer with the School Phone already typed in
    }
}