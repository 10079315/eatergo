package com.example.likaiapply.eatergo;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by likaiapply on 2017/5/11.
 */

public class HtmlTextView extends android.support.v7.widget.AppCompatTextView {


        public HtmlTextView(Context context) {
            super(context);
        }

        public HtmlTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        /**
         * Parses String containing HTML to Android's Spannable format and displays
         * it in this TextView.
         *
         * @param html String containing HTML, for example: "<b>Hello world!</b>"
         */
        public void setHtmlFromString(String html, boolean useLocalDrawables) {
            Html.ImageGetter imgGetter;
            if (useLocalDrawables) {
                imgGetter = new LocalImageGetter(getContext());
            } else {
                imgGetter = new UrlImageGetter(this, getContext());
            }
            // this uses Android's Html class for basic parsing, and HtmlTagHandler
            setText(Html.fromHtml(html, imgGetter, new HtmlTagHandler()));

            // make links work
            setMovementMethod(LinkMovementMethod.getInstance());

        }

}
