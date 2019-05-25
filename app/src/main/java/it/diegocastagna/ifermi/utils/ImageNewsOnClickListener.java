package it.diegocastagna.ifermi.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import it.diegocastagna.ifermi.activity.PopupActivity;

public class ImageNewsOnClickListener implements View.OnClickListener {
    private String imageUrl;
    private String title;
    private String desc;
    private Context context;

    public ImageNewsOnClickListener(String imageUrl, String title, String desc, Context context) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, PopupActivity.class);
        i.putExtra(PopupActivity.TYPE_STRING, PopupActivity.TYPE_NEWS);
        i.putExtra("imageUrl", imageUrl);
        i.putExtra("title", title);
        i.putExtra("description", desc);
    }
}
