package com.example.likaiapply.eatergo;

import android.content.Context;

/**
 * Created by yangyanzhou on 5/9/17.
 */

public class Equations {
    public static int dpToPx(Context context, float dp) {
        // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
}
