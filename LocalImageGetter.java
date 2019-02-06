package com.example.likaiapply.eatergo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;


public class LocalImageGetter implements Html.ImageGetter {

    Context c;

    public LocalImageGetter(Context c) {
        this.c = c;
    }

    @Override
    public Drawable getDrawable(String source) {
        int id = c.getResources().getIdentifier(source, "drawable", c.getPackageName());
        if (id == 0) {
            // the drawable resource wasn't found in our package, maybe it is a
            // stock android drawable?
            id = c.getResources().getIdentifier(source, "drawable", "android");
        }
        if (id == 0) {
            // prevent a crash if the resource still can't be found
            Log.i("rew","source could not be found: " + source);
            return null;
        } else {
            Drawable d = c.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }
}
