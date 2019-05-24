package it.diegocastagna.ifermi.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
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

        createViewsNews();
        return parent;
    }

    private void createViewsNews(){
        LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.VERTICAL);

        Model mModel = Model.getInstance();
        ArrayList l = mModel.getNewsList();

        if(!l.isEmpty()) {
            for (Object o : l) {
                RssNews r = (RssNews) o;

                LinearLayout imageLayout = new LinearLayout(context);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageLayout.setLayoutParams(imageParams);
                imageLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Children of the Parent LinearLayout
                ImageView iv = new ImageView(context);
                downloadSetupImage(mModel.IMAGERSSURL + r.getIconId(), iv);

                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 0, 0, 0);
                layout.setLayoutParams(layoutParams);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Children of the layout LinearLayout
                TextView title = new TextView(context);
                title.setText(r.getTitle());
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setTextSize(15);

                TextView description = new TextView(context);
                description.setText(r.getShortDesc());

                parent.addView(imageLayout);
                imageLayout.addView(iv);
                imageLayout.addView(layout);
                layout.addView(title);
                layout.addView(description);
            }
        } else {
            TextView title = new TextView(context);
            title.setText("Nessuna news trovata");
            parent.addView(title);
        }

        main.setNewsContainer(parent);
    }

    // Non funziona se non nel Main ._.
    protected void downloadSetupImage(String imageURL, ImageView target){
        Picasso.get().load(imageURL).into(target);
    }

    @Override
    protected void onPostExecute(Object o) {
        main.setNewsContainer((LinearLayout) o);
    }
}
