package it.diegocastagna.ifermi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import it.diegocastagna.ifermi.activity.MainActivity;
import it.diegocastagna.ifermi.models.Model;

public class RssNewsAsync extends AsyncTask {
    private MainActivity main;
    private Context context;
    private LinearLayout parent;

    @Override
    protected Object doInBackground(Object[] objects) {
        main = (MainActivity) objects[0];
        context = (Context) objects[1];

        parent = new LinearLayout(context);
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);

        createViews();
        return parent;
    }

    private void createViews(){
        Model mModel = Model.getInstance(); // Model
        ArrayList l = mModel.getNewsList();

        if(!l.isEmpty()) {
            for (Object o : l) {
                RssNews r = (RssNews) o;

                // Children of the Parent LinearLayout
                ImageView iv = new ImageView(context);
                downloadSetupImage(Model.imageRssUrl + r.getIconId(), iv);
                LinearLayout layout = new LinearLayout(context);

                parent.addView(iv);

                // Children of the layout LinearLayout
                TextView title = new TextView(context);
                title.setText(r.getTitle());
                TextView description = new TextView(context);
                description.setText(r.getDescription());

                layout.addView(title);
                layout.addView(description);
                parent.addView(layout);
            }
        } else {
            TextView title = new TextView(context);
            title.setText("Nessuna news trovata");
            parent.addView(title);
        }
    }

    protected void downloadSetupImage(String imageURL, ImageView target){
        Picasso.get().load(imageURL).into(target);
    }

    @Override
    protected void onPostExecute(Object o) {
        main.setNewsContainer((LinearLayout) o);
    }
}
